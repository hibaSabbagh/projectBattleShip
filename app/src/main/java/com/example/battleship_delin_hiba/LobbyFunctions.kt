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
                                                                                                          // Andra skärm som visas
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()        //ett sätt att samla info om spelare & aktiva matcher och göra tillgänglig till UI
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    var showChallengePopup by remember { mutableStateOf(false) }                //för popup om man har fått en challenge, false då man har ej fått en
    //var currentBattleId by remember { mutableStateOf("") }


//om den lokala spelare är en av spelarna och deras tur så går man till setUppBoard
//annars om spelare2 är aktiv så visas popup
//    LaunchedEffect(battles) {
//        battles.forEach { (gameId, battle) ->
//            if ((battle.player1Id == model.localPlayerId.value || battle.player2Id == model.localPlayerId.value) && battle.gamestate == "player1_turn") {
//                navController.navigate("SetUpBoard")
//            } else if (battle.player2Id == model.localPlayerId.value && battle.gamestate == "Invite") {
//                showChallengePopup = true
//                model.localBattleId.value = gameId
//            }
//        }
//    }

//om inget namn tilldelas då unknown
//även kollar om spelare finns med i lista då tilldelas spelaren det namnet som matchar sin id

    var playerName = "Unknown?"
    players[model.localPlayerId.value]?.let{
        playerName = it.name
    }





    Scaffold(                                                       //för tillbak knappen och online cirkel
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.padding(100.dp))
                        Text(text = "Online")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Box(modifier = Modifier.size(15.dp).clip(CircleShape).background(Color.Green))
                    }
                },
                navigationIcon = {
                    IconButton( onClick = { handleLeaveLobby(navController, model, sharedPreferences) })     //om man trycker på tillbaka knappen så skickas man tillbaka till main
                    {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { handleLeaveLobby(navController, model, sharedPreferences)},    //om man trycker på leave lobby så skickas man tillbaka till main
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {Text("Leave Lobby")}
            )
       },
        content = { padding -> PlayerListLoop(padding, navController, model)},
        bottomBar = {
            BottomAppBar(                                                                                 //för antal spelare i lobby och båten
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
    if (showChallengePopup) {   navController.navigate("Battle")                                          //om man accepterar challenge så visas popup
//        ChallengePopup(
//            navController = navController,
//            model = model,
//            onDismiss = { showChallengePopup = false }
//        )
    }
}






//visar en lista av spelare och loopar igenom alla spelare i playerMap,
//om lokal spelare visas som you,
//för andra spelare antingen om aktiv inbjuda finns så waiting eller om aktiv inbjuda inte finns challenge knappen
@Composable
fun PlayerListLoop( padding : PaddingValues, navController: NavController, model : GameModel) {
    val playerMapCpy by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()

    LazyColumn( modifier = Modifier.fillMaxSize().padding(padding)) {
        items(playerMapCpy.entries.toList()) { player ->
            if (player.key == model.localPlayerId.value) {                         //kollar om spelarensId matchar den lokala spelareId
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = " ${playerMapCpy[model.localPlayerId.value]?.name} (you)") })           //om det matchar så visas namn med (you)
            } else {
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = player.value.name) },
                    trailingContent = { var hasGame = false                                                                    //annars loopar igenom matcher och kollar om någon har invitet till spelet
                        battles.forEach { (gameId, battle) ->
                            if (battle.player1Id == model.localPlayerId.value && battle.gamestate == "Invite") {
                                hasGame = true
                                Text("Waiting for accept...")
                            }
                        }
                        if (!hasGame) {                                                                                 //och sen kollar om man inte är med i någon match
                            Button(
                                onClick = {
                                    model.db.collection("battles").add(
                                        Battle(
                                            gamestate = "Invite",                                                      //inbjudan skickas och player1Id ändras till localPlayerId
                                            player1Id = model.localPlayerId.value!!,
                                            player2Id = player.key)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors( containerColor = Color(0xFFD3368E)),
                            ) { Text(text = "Challenge") }
                        }
                    }
                )
            }
        }
    }
}







//@Composable
//fun  ChallengePopup(navController: NavController, model: GameModel, onDismiss: ()-> Unit){
//    AlertDialog(
//        onDismissRequest = { onDismiss() },
//        title = { Text("Challenge Received") },
//        text = { Text("Do you accept the challenge?") },
//        confirmButton = {
//            Button(
//                onClick = {
//                    model.db.collection("battles").document(model.localBattleId.value!!).update("gamestate", "player1_turn").addOnSuccessListener {
//                        //model.localBattleId.value = battleId
//                        navController.navigate("SetUpBoard") }
//                    onDismiss()
//                }
//            ) { Text("Accept") }
//        },
//        dismissButton = {
//            Button(
//                onClick = {
//                    model.db.collection("battles").document(model.localBattleId.value!!).delete()
//                    onDismiss() }
//            ) { Text("Decline") }
//        }
//    )
//}
    //här hanteras popup, om man accepterar så uppdateras matchen i databasen och player1 tur, sen onDismiss tar bort popup
    // om man inte accepterar så tas bort matchen från databasen sen onDismiss tar bort popup






fun handleLeaveLobby(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences){
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
//Kollar om det finns ett giltigt spelar-ID:
//Om ja, fortsätter processen.
//annars Tar bort spelaren från databasen. Nollställer det lokala spelar-ID:t i appen. Tar bort spelarens ID från databasen
//Navigerar användaren tillbaka till main

