package com.example.battleship_delin_hiba

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(navController: NavController, model: GameModel, battleId: String?) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    val battle = battles[model.localBattleId.value]
    val localBattleId = model.localBattleId.value ?: return
    var playerBoard: MutableList<Int>?
    var opponentBoard: MutableList<Int>?
    if (battles[model.localBattleId.value]?.player1Id == model.localPlayerId.value) {
        playerBoard = battles[model.localBattleId.value]?.gameBoardP1 as MutableList<Int>
        opponentBoard = battles[model.localBattleId.value]?.gameBoardP2 as MutableList<Int>
    } else {
        playerBoard = battles[model.localBattleId.value]?.gameBoardP2 as MutableList<Int>
        opponentBoard = battles[model.localBattleId.value]?.gameBoardP1 as MutableList<Int>
    }

    var playerId by remember {
        mutableStateOf(
            if (battle?.player1Id == model.localPlayerId.value)
                battle?.player1Id else battle?.player2Id
        )
    }
    var opponentId by remember {
        mutableStateOf(
            if (battle?.player1Id == model.localPlayerId.value)
                battle?.player2Id else battle?.player1Id
        )
    }

    val myTurn = battle?.gameState == GameState.player1_turn && battle.player1Id == playerId
            || battle?.gameState == GameState.player2_turn && battle.player2Id == playerId


    LaunchedEffect(battles) {
        if(battle == null){
            navController.navigate("Lobby")
        } else (battle)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "${battle?.gameState}")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("Lobby") }       //kanske till main
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    handleLeaveGame(
                        navController,
                        model
                    )
                },            //kanske till main
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = { Text("Leave Game") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "person",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "${players[playerId]?.name}")
                }
                Spacer(modifier = Modifier.width(30.dp))             //space mellan player1 och vs.
                Text(
                    text = "vs.",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(40.dp),
                )
                Spacer(modifier = Modifier.width(30.dp))            //space mellan vs. och player2
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "person",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "${players[opponentId]?.name}")
                }
            }
            Spacer(modifier = Modifier.height(30.dp))  //stora bräda

            LazyVerticalGrid(
                columns = GridCells.Fixed(10),
                modifier = Modifier
                    .size((BoardConstants.CELL_SIZE * 10).dp)
                    .padding(start = 50.dp, end = 50.dp)
//                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Black)
            ) {
                if (true) {
                    items(opponentBoard.size) { item ->
                        Box(
                            modifier = Modifier
                                .size(BoardConstants.CELL_SIZE.dp)
                                .background(
                                    when (opponentBoard[item]) {
                                        2 -> Color.Red
                                        -1 -> Color.Blue
                                        else -> Color.White
                                    }
                                )
                                .border(1.dp, Color.Black)
                                .clickable(
                                    enabled = (opponentBoard[item] == 0 || opponentBoard[item] == 1) && myTurn
                                ) {
                                    model.handleCellClick(item, localBattleId)
                                    Log.d("BattleScreen", "Tile clicked at index: $item")
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))    //lilla bräda

            LazyVerticalGrid(
                columns = GridCells.Fixed(10),
                modifier = Modifier
                    .size(
                        width = (100).dp,
                        height = 150.dp
                    )
                    .padding(bottom = 10.dp)
                    .border(1.dp, Color.Black)
            ) {
                if (true) {
                    items(playerBoard.size) { item ->
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(
                                    when (playerBoard[item]) {
                                        1 -> Color.Gray
                                        2 -> Color.Red
                                        -1 -> Color.Blue
                                        else -> Color.White
                                    }
                                )
                                .border(1.dp, Color.Black)

                        )
                    }
                }
            }
        }
    }
}

    fun handleLeaveGame(navController: NavController, model: GameModel) {
        if (model.localBattleId.value != null) {
            model.db.collection("battles").document(model.localBattleId.value!!)
                .update("gameState", GameState.Cancelled).addOnSuccessListener {
                    model.localBattleId.value = null
                    navController.navigate("Lobby") {
                        popUpTo("Battle") { inclusive = true }
                    }
                }.addOnFailureListener {
                    Log.e("BattleScreen", "Failed to delete battle", it)
                }
        } else {
            Log.w("BattleScreen", "Current battle ID is null")
            model.localBattleId.value = null
            navController.navigate("Lobby") {
                popUpTo("Battle") { inclusive = true }
            }
        }
    }


