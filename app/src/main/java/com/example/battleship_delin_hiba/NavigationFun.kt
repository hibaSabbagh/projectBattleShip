package com.example.battleship_delin_hiba

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*

data class Player (
    val name: String = "",
    var status : String = ""            //status kan vara online eller inbattle
)

data class Battle(   //lägg till game board med en list eller en mapp
    val player1Id: String = "",
    val player2Id: String = "",
    val gamestate: String = "Invite",  //kan bli enum  "player1_turn", "player2_turn", "player1_win", "player2_win", "draw"
    var gameBoard: List<Int> = List(100) {0}
)


@Composable
fun BattleNav(){
    val navController = rememberNavController()
    val model = GameModel()
    model.initGame()
    val sharedPreferences = LocalContext.current.getSharedPreferences("BattelShipPrefs", Context.MODE_PRIVATE)

    NavHost(navController = navController, startDestination = "Main"){   //lägger in model i alla
        composable("Main"){ MainScreen(navController,model, sharedPreferences) }
        composable("Lobby"){ LobbyScreen(
            navController,
            model,
            sharedPreferences) }
        composable("SetUpBoard"){ SetUpBoardScreen(navController,model) }
        composable("Battle"){ backStackEntry -> val battleID = backStackEntry.arguments?.getString("battleId")
            BattleScreen(navController, model) }
    }
}

