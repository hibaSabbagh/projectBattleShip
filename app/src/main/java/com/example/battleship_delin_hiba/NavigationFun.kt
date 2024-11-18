package com.example.battleship_delin_hiba

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*

data class Player (
    val name: String,
    val id: String,
    var status : String
)

data class Battle(
    val player1: Player,
    val player2: Player,
    val gameID: String
)



@Composable
fun BattleNav(){
    val navController = rememberNavController()
    val playerList = remember {mutableStateListOf<Player>() }
    val battlesList = remember {mutableStateListOf<Battle>()}

    NavHost(navController = navController, startDestination = "Main"){
        composable("Main"){ MainScreen(navController,playerList) }
        composable("Lobby"){ LobbyScreen(navController,playerList, battlesList) }
        composable("Battle"){ BattleScreen(navController) }

    }


}

