package com.example.battleship_delin_hiba

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.UUID

@Composable
fun MainScreen(navController: NavController, playerList: MutableList<Player>){
    /* there is an action button that takes you to the lobby screen
    * */
    var playerName by remember { mutableStateOf("") }
    Scaffold() { padding -> Column (modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        TextField (value = playerName, onValueChange = { playerName = it }, label = { Text("Name") })
                    Spacer (modifier = Modifier.height(16.dp))
    }
        if() {
            val playerUniqueID: String = UUID.randomUUID().toString()
            val player: Player = Player(playerName, playerUniqueID) // picture of the game
            playerList.add(player)
            // text field to write name

            // button to register

            JoinGameButton(onClick = { navController.navigate("lobby") })
        }
    }
}

@Composable
fun JoinGameButton(onClick: ()->Unit){

        Button(
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB60D51)),
            modifier = Modifier
                .width(200.dp)
                .height(100.dp).padding(16.dp).offset(x =100.dp,y= 100.dp)
        )
        {
            Text(text = "Join Game")
        }

}

