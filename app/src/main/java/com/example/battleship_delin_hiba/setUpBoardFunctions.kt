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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.asStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController, model: GameModel) {
    //ett sätt att samla info om spelare & aktiva matcher och göra tillgänglig till UI
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    var gameBoard by remember { mutableStateOf(model.placeShipInBoard(model._ships).toMutableList())}
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
                    columns = GridCells.Fixed(10), modifier = Modifier.fillMaxSize()
                ) {
                    items(gameBoard.size) { item ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (gameBoard[item] == 1) Color.Gray else Color.White)
                                .border(1.dp, Color.Black)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = {
                                            draggingShip = findShipAtPosition(item, model._ships)
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

fun findShipAtPosition(index: Int, ships: List<Ship>): Ship?{
    return ships.find { ship ->
        val positions = getShipPositions(ship)
        index in positions

    }
}

fun getShipPositions(ship: Ship): List<Int>{
    return if ( ship.orientation == Orientation.HORIZONTAL)
    {
        (ship.start until ship.start + ship.size).toList()
    }else { (0 until ship.size).map {ship.start +it*10 }

    }
}

fun calculateNewStartPosition(ship: Ship, dragAmount:Offset, boardSize: Int): Int {
    val rowSize = 10
    val column = ship.start % rowSize
    val row = ship.start / rowSize
    val targetColumn = (column + (dragAmount.x / 40).toInt()).coerceIn(0,rowSize-1)
    val targetRow = (row + (dragAmount.y / 40).toInt()).coerceIn(0,boardSize/ rowSize-1)
    return targetRow * rowSize + targetColumn
}

fun isValidPosition(start: Int, size: Int, orientation: Orientation, boardSize: Int): Boolean {
    val rowSize = 10
    return (if  (orientation == Orientation.HORIZONTAL) {
        val end = start+size-1
        end < boardSize && (start / rowSize) == (end / rowSize)
    }else {
        val end = start +(size-1)*rowSize
        end < boardSize
    })

}

fun updateBoardWithShip(board: List<Int>, ship: Ship, newStart:Int): MutableList<Int> {
    val newBoard = board.toMutableList()
    val oldPositions = getShipPositions(ship)
    val newPositions = if(ship.orientation == Orientation.HORIZONTAL){
        (newStart until newStart + ship.size).toList()
    }else {
        (0 until ship.size).map{newStart + it*10}
    }
    oldPositions.forEach{newBoard[it] = 0}
    newPositions.forEach{newBoard[it] = 1}
    return newBoard
}

fun handleStartGame(navController: NavController,
                    model: GameModel,
                    battles: Map<String, Battle>,
                    gameBoard: List<Int>) {

    //"gameBoardP1" to model.placeShipInBoard(model._ships)

    if (battles[model.localBattleId.value]?.player1Id == model.localPlayerId.value ) {
        model.db.collection("battles").document(model.localBattleId.value!!)
            .update("gameBoardP1", gameBoard).addOnSuccessListener {

            if (battles[model.localBattleId.value]?.gameState == GameState.accepted) {
                model.db.collection("battles").document(model.localBattleId.value!!)
                    .update("gameState", GameState.waiting_for_opponent)
            } else if (battles[model.localBattleId.value]?.gameState == GameState.waiting_for_opponent) {
                model.db.collection("battles").document(model.localBattleId.value!!)
                    .update("gameState", GameState.player1_turn)
            }
            navController.navigate("Battle")
        }

    } else if (battles[model.localBattleId.value]?.player2Id == model.localPlayerId.value) {
        model.db.collection("battles").document(model.localBattleId.value!!)
            .update("gameBoardP2", gameBoard).addOnSuccessListener {
            if (battles[model.localBattleId.value]?.gameState == GameState.accepted) {
                model.db.collection("battles").document(model.localBattleId.value!!)
                    .update("gameState", GameState.waiting_for_opponent)
            } else if (battles[model.localBattleId.value]?.gameState == GameState.waiting_for_opponent) {
                model.db.collection("battles").document(model.localBattleId.value!!)
                    .update("gameState", GameState.player1_turn)
            }
            navController.navigate("Battle")
        }
    }
}


   /*
    *    1 0 0 0 1 1 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 1
    *    1 0 0 0 0 0 0 0 0 1
    *    1 0 0 0 0 0 0 0 0 1
    *    1 0 0 0 1 0 0 0 0 0
    *    1 0 0 0 0 0 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 0
    *    0 0 0 1 1 0 0 0 0 0
    */
