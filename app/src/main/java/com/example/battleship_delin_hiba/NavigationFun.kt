package com.example.battleship_delin_hiba

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*

data class Player (                         //data klass som representerar en spelare och namn en tom sträng som default
    val name: String = "",
    var status : String = ""
)

data class Battle(                            //data klass som representerar en battle och defaultvärden
    val player1Id: String = "",
    val player2Id: String = "",
    val gamestate: String = "Invite",
    var gameBoardP1: List<Int> = List(100) {0},
    var gameBoardP2: List<Int> = List(100) {0}
)


@Composable
fun BattleNav(){                                                           //hanterar navigationen mellan olika skärmar och skickar meddelanden mellan de
    val navController = rememberNavController()
    val model = GameModel()
    model.initGame()
    val sharedPreferences = LocalContext.current.getSharedPreferences("BattleShipPrefs", Context.MODE_PRIVATE)

    NavHost(navController = navController, startDestination = "Main"){   //lägger in model i alla
        composable("Main"){ MainScreen(navController,model, sharedPreferences) }
        composable("Lobby"){ LobbyScreen(
            navController,
            model,
            sharedPreferences) }
        composable("SetUpBoard"){ SetUpBoardScreen(navController,model) }
        composable("Battle"){ /*backStackEntry -> val battleID = backStackEntry.arguments?.getString("battleId")*/
            BattleScreen(navController, model) }
    }
}

