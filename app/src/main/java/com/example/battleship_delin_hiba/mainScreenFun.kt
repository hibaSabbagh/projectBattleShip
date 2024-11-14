package com.example.battleship_delin_hiba

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.UUID

@Composable
fun MainScreen(navController: NavController, playerList: MutableList<Player>) {
    /* there is an action button that takes you to the lobby screen
    * */
    var playerName by remember { mutableStateOf("") }
    Scaffold() { padding ->
        MyImage()
        Column(

            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Name") })
            Spacer(modifier = Modifier.height(16.dp))
            JoinGameButton(onClick = {navController.navigate("lobby")})

        }
        if (playerName.isNotEmpty() && !playerList.any { it.name == playerName }) {
            val playerUniqueID: String = UUID.randomUUID().toString()
            val player: Player = Player(playerName, playerUniqueID) // picture of the game
            playerList.add(player)
            // text field to write name

            // button to register

        }
    }
}

@Composable
fun MyImage(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.shippic),
            contentDescription = "main screen picture of the game",             //beskriver bilden
            modifier = Modifier
            .padding(top = 100.dp)
            .size(350.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun RegisterPlayer (playerList: MutableList<Player>){
    // we will validate the name from here as you write the system looks in the database if there is
    // a matching name and  it lets you know
    val playerUniqueID : String = UUID.randomUUID().toString()
    val playerName : String = readLine().toString()
    val player : Player = Player( playerName, playerUniqueID)
    playerList.add(player)

}

@Composable
fun JoinGameButton(onClick: ()->Unit){

        Button(
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB60D51)),
            modifier = Modifier
                .width(200.dp)
                .height(100.dp).padding(16.dp).offset(x =100.dp,y= 250.dp)
        )
        {
            Text(text = "Join Game")
        }

}

