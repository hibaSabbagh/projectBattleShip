package com.example.battleship_delin_hiba



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController,model: GameModel) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "Set Up Board")
                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                ExtendedFloatingActionButton(
                    onClick = { handleStartGame(navController, model) }, // handel start game
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD3368E),
                    contentColor = Color.Black,
                    content = { Text("Start Game") }
                )
            }
        },
        content = { padding ->
            Column (modifier = Modifier.padding(padding).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                LazyVerticalGrid(modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 50.dp)
                        .fillMaxWidth().height(400.dp).clickable( onClick = { navController.navigate("Battle") }),
                    columns = TODO(), content = { }  )
            }
        }
    )
}


fun handleStartGame(navController: NavController, model: GameModel){
    model.localBattleId.value?.let {
        navController.navigate("Battle")
    }
}


   /*
    *    1 0 0 0 1 1 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 1
    *    1 0 0 0 0 0 0 0 0 1
    *    1 0 0 0 0 0 0 0 0 1
    *    1 0 0 0 1 0 0 0 0 0
    *    1 0 0 0 0 0 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 0
    *    0 0 0 0 0 0 0 0 0 0
    *    0 0 0 1 1 0 0 0 0 0
    */
