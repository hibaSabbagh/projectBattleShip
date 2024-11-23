package com.example.battleship_delin_hiba

import android.R.attr.padding
import android.R.attr.title
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    var showChallengePopup by remember { mutableStateOf(false) }
    var currentBattleId by remember { mutableStateOf("") }

    LaunchedEffect(battles) {
        battles.forEach { (gameId, battle) ->
            if ((battle.player1Id == model.localPlayerId.value || battle.player2Id == model.localPlayerId.value) && battle.gamestate == "player1_turn") {
                navController.navigate("SetUpBoard")
            } else if (battle.player2Id == model.localPlayerId.value && battle.gamestate == "Invite") {
                showChallengePopup = true
                currentBattleId = gameId
            }
        }
    }

    var playerName = "Unknown?"
    players[model.localPlayerId.value]?.let{
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
                        Spacer(modifier = Modifier.padding(100.dp))             //hur mycket utrymme mellan row och column
                        Text(text = "Online")
                        Spacer(modifier = Modifier.padding(5.dp))               //space mellan gröna cirkel och online
                        Box(
                            modifier = Modifier.size(15.dp).clip(CircleShape).background(Color.Green)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { handleLeaveLobby(navController, model, sharedPreferences) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { handleLeaveLobby(navController, model, sharedPreferences)},   // we can change this when when we connect to the server
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {Text("Leave Lobby")}

            )
       },
        content = { padding -> PlayerListLoop(padding, navController, model)},
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text (text = "${battles.size} active games")
                        Icon( painter = painterResource(id = R.drawable.directions_boat),
                            contentDescription = "boat")
                    }
                }
            )
       }
    )
    if (showChallengePopup) {
        ChallengePopup(
            navController = navController,
            model = model,
            battleId = currentBattleId,
            onDismiss = { showChallengePopup = false }
        )
    }
}





@Composable
fun PlayerListLoop( padding : PaddingValues, navController: NavController, model : GameModel) {
    val playerMapCpy by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()

    LazyColumn( modifier = Modifier.fillMaxSize().padding(padding)) {
        items(playerMapCpy.entries.toList()) { player ->
            if (player.key == model.localPlayerId.value) {
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = " ${playerMapCpy[model.localPlayerId.value]?.name} (you)") })
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
                        var hasGame = false /// might change to mutableStateOf(false)
                        battles.forEach { (gameId, battle) ->
                            if (battle.player1Id == model.localPlayerId.value && battle.gamestate == "Invite") {
                                hasGame = true
                                Text("Waiting for accept...")
                            }
                        }
                        if (!hasGame) {
                            Button(
                                onClick = {
                                    model.db.collection("battles").add(
                                        Battle(
                                            gamestate = "Invite",
                                            player1Id = model.localPlayerId.value!!,
                                            player2Id = player.key
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors( containerColor = Color(0xFFD3368E)),
                            ) {
                                Text(text = "Challenge") }
                        }
                    }
                )
            }
        }
    }
}



@Composable
fun  ChallengePopup(navController: NavController, model: GameModel, battleId: String, onDismiss: ()-> Unit){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Challenge Received") },
        text = { Text("Do you accept the challenge?") },
        confirmButton = {
            Button(
                onClick = {
                    model.db.collection("battles").document(battleId).update("gamestate", "player1_turn").addOnSuccessListener {
                        navController.navigate("SetUpBoard")
                    }
                    onDismiss()
                }
            ) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    model.db.collection("battles").document(battleId).delete()
                    onDismiss()
                }
            ) {
                Text("Decline")
            }
        }
    )
}


fun handleLeaveLobby(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences){

    model.localPlayerId.value?.let {
        model.db.collection("players").document(it).delete().addOnSuccessListener {
            model.localPlayerId.value = null
            sharedPreferences.edit().remove("playerId").apply()
            navController.navigate("Main") {
                popUpTo("Lobby") { inclusive = true }
            }
        }            // removes player from database
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun challengePopup(navController: NavController, model: GameModel) : Boolean {
//    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
//    battles.forEach { (gameId, battle) ->
//        model.db.collection("battles").document(gameId).update("gamestate", "player1_turn")
//        return false
//    }
//    return true
//}