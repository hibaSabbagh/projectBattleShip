package com.example.battleship_delin_hiba

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, playerList: MutableList<Player>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                        Spacer(modifier = Modifier.padding(100.dp))             //hur mycket utrymme mellan row och column
                        Text(text = "Online")
                        Spacer(modifier = Modifier.padding(5.dp))               //space mellan gröna cirkel och online
                        Box(
                            modifier = Modifier.size(15.dp).clip(CircleShape).background(Color.Green)
                        )
                    }
                }
            )
        },
        content = { padding ->
            Button(
                onClick = { navController.navigate("mainScreen")},
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB60D51)),
                modifier = Modifier.width(200.dp).height(100.dp).padding(16.dp).offset(x = 210.dp, y = 800.dp)
            ){
                Text(text = "Leave Game")
            }
        }
    )
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