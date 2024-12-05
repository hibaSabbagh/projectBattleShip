package com.example.battleship_delin_hiba


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences,) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    var showChallengePopup by remember { mutableStateOf(false) }
    var currentBattleId by model.localBattleId
    LaunchedEffect(battles) {
        battles.forEach { (gameId, battle) ->
            if ((battle.player1Id == model.localPlayerId.value || battle.player2Id == model.localPlayerId.value) && battle.gameState == GameState.accepted) {
                navController.navigate("SetUpBoard")
            } else if (battle.player2Id == model.localPlayerId.value && battle.gameState == GameState.Invite) {
                showChallengePopup = true
                currentBattleId = gameId
            }
        }
    }
    var playerName = "Unknown?"
    players[model.localPlayerId.value]?.let {
        playerName = it.name
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(100.dp))
                        Text(text = "Online")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        handleLeaveLobby(
                            navController,
                            model,
                            sharedPreferences
                        )
                    })
                    {
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
                    handleLeaveLobby(
                        navController,
                        model,
                        sharedPreferences
                    )
                },    //om man trycker på leave lobby så skickas man tillbaka till main
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = { Text("Leave Lobby") }
            )
        },
        content = { padding -> PlayerListLoop(padding, model) },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${battles.size} active games")
                        Icon(
                            painter = painterResource(id = R.drawable.directions_boat),
                            contentDescription = "boat"
                        )
                    }
                }
            )
        }
    )
    if (showChallengePopup) {
        ChallengePopup(
            navController = navController,
            model = model,
            battleId = currentBattleId.toString(),
            onDismiss = { showChallengePopup = false }
        )
    }
}
@Composable
fun PlayerListLoop(padding: PaddingValues, model: GameModel) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(players.entries.toList()) { player ->
            if (player.key == model.localPlayerId.value) {                         //kollar om spelarensId matchar den lokala spelareId
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = " ${players[model.localPlayerId.value]?.name} (you)") })
            } else {
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = player.value.name) },
                    trailingContent = {
                        var hasGame = false
                        battles.forEach { (gameId, battle) ->
                            if (battle.player1Id == model.localPlayerId.value && battle.gameState == GameState.Invite) {
                                model.localBattleId.value = gameId
                                hasGame = true
                                Text("Waiting for accept...")
                            }
                        }
                        if (!hasGame) {
                            Button(
                                onClick = {handlePressChallenge(model, player.key, battles)},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD3368E
                                    )
                                ),
                            ) { Text(text = "Challenge") }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChallengePopup(navController: NavController, model: GameModel, battleId: String, onDismiss: () -> Unit, ){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Challenge Received") },
        text = { Text("Do you accept the challenge?") },
        confirmButton = {
            Button(
                onClick = {
                    model.db.collection("battles").document(battleId)
                        .update("gameState", GameState.accepted).addOnSuccessListener {
                            model.localBattleId.value = battleId
                            navController.navigate("SetUpBoard")
                        }
                    onDismiss()
                }
            ) { Text("Accept") }
        },
        dismissButton = {
            Button(
                onClick = {
                    model.db.collection("battles").document(battleId).delete()
                    onDismiss()
                }
            ) { Text("Decline") }
        }
    )
}




fun handleLeaveLobby(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences, ){
    model.localPlayerId.value?.let {
        model.db.collection("players").document(it).delete().addOnSuccessListener {
            model.localPlayerId.value = null
            sharedPreferences.edit().remove("playerId").apply()
            navController.navigate("Main") {
                popUpTo("Lobby") { inclusive = true }
            }
        }
    }
}




fun handlePressChallenge(model: GameModel, key : String, battles: Map<String, Battle>){
    val gameData = hashMapOf(
        "gameState" to GameState.Invite,
        "player1Id" to model.localPlayerId.value,
        "player2Id" to key,
        "gameBoardP1" to model.placeShipInBoard(model._ships),
        "gameBoardP2" to model.placeShipInBoard(model._ships)
    )
    model.db.collection("battles").add(gameData).addOnFailureListener{
        Log.e("Firebase", "Error adding document: $it")
    }
}

