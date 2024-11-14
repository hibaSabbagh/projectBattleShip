package com.example.battleship_delin_hiba

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, playerList: MutableList<Player>){
    /* each player has a button(challenge) that takes you to the battle screen
    * */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Online") },
                navigationIcon = { IconButton(
                    onClick = { navController.navigate("Main") },

                ){
                    Icon (Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                }
                }
            )
        }


    ) {}

}


@Composable
fun PlayerListLoop(playerList: MutableList<Player>){
    for (player in playerList){

    }

}

@Composable
fun  ChallengeButton(player: Player){}

@Composable
fun ActiveGames (player: Player, battlesMap: MutableMap<String, Battle>){

}

@Composable
fun ChallengePopup(){

}