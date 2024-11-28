package com.example.battleship_delin_hiba



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController,model: GameModel) {
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
                        .fillMaxWidth().height(400.dp),
                    columns = TODO()
                ){}
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
    *
    *
    * */
