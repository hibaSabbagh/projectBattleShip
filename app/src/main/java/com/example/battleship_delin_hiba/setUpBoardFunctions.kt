package com.example.battleship_delin_hiba



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.asStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController, model: GameModel) {
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    var gameBoard by remember { mutableStateOf(placeShipInBoard(_ships).toMutableList())}
    var draggingShip by remember { mutableStateOf<Ship?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Set Up Board")
                }
            })
        }, floatingActionButton = {
            Column {
                ExtendedFloatingActionButton(onClick = { handleStartGame(navController, model, battles, gameBoard) },                           // handel start game
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD3368E),
                    contentColor = Color.Black,
                    content = { Text("Start Game") })
            }
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    modifier = Modifier
                        .size((BoardConstants.CELL_SIZE * 10).dp)
                        .fillMaxSize()
                ) {
                    items(gameBoard.size) { item ->
                        Box(
                            modifier = Modifier
                                .size(BoardConstants.CELL_SIZE.dp)
                                .background(if (gameBoard[item] == 1) Color.Gray else Color.White)
                                .border(1.dp, Color.Black)
                                .pointerInput(Unit) {
                                    detectDragGestures(

                                        onDragStart = {
                                            draggingShip = findShipAtPosition(item,_ships)
                                        },
                                        onDragEnd = { draggingShip = null },
                                        onDragCancel = { draggingShip = null },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            draggingShip?.let { ship ->
                                                val newStart = calculateNewStartPosition(
                                                    ship,
                                                    dragAmount,
                                                    gameBoard.size
                                                )
                                                if (isValidPosition(
                                                        newStart,
                                                        ship.size,
                                                        ship.orientation,
                                                        gameBoard.size
                                                    )
                                                ) {
                                                    gameBoard = updateBoardWithShip(
                                                        gameBoard,
                                                        ship,
                                                        newStart
                                                    )
                                                    draggingShip = ship.copy(start = newStart)
                                                }
                                            }
                                        }
                                    )
                                }
                        )
                    }
                }
            }
        }
    )
}

fun handleStartGame(navController: NavController, model: GameModel, battles: Map<String, Battle>, gameBoard: List<Int>) {
    val battleId = model.localBattleId.value ?: return
    val localPlayerId = model.localPlayerId.value ?: return
    val battle = battles[battleId] ?: return
    val isPlayer1 = battle.player1Id == localPlayerId
    val gameBoardField = if (isPlayer1) "gameBoardP1" else "gameBoardP2"
    val newGameState = when (battle.gameState) {
        GameState.accepted -> GameState.waiting_for_opponent
        GameState.waiting_for_opponent -> GameState.player1_turn
        else -> battle.gameState
    }
    model.db.collection("battles").document(battleId).
    update(gameBoardField, gameBoard,
        "gameState", newGameState).addOnSuccessListener{
        navController.navigate("Battle/$battleId")
    }
}