package com.example.battleship_delin_hiba



import android.icu.text.Transliterator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController, model: GameModel) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    val gameBoard = remember { mutableStateListOf(*List(100) { 0 }.toTypedArray()) }
    val ships = remember {
        mutableStateListOf(
            Ship(size = 4, start = 0),
            Ship(size = 3, start = 20),
            Ship(size = 2, start = 40),
            Ship(size = 2, start = 50),
            Ship(size = 1, start = 60),
            Ship(size = 1, start = 70)
        )
    }

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
                    onClick = { handleStartGame(navController, model, ships, gameBoard) }, // handel start game
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD3368E),
                    contentColor = Color.Black,
                    content = { Text("Start Game") }
                )
            }
        },
        content = { padding ->
            Column (modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    modifier = Modifier.fillMaxSize()
                ){
                    items(100){ index ->
                        Box (
                            modifier = Modifier
                                .size(40.dp)
                                .background(if(gameBoard[index] == 0){Color.White}
                                    else {Color.Gray})
                                .border(1.dp, Color.Black)
                                .clickable{
                                    val selectedShip = ships.firstOrNull{ it.start == index}
                                    if(selectedShip != null){
                                        model.moveShip(selectedShip,index, gameBoard)
                                    }
                                }

                        )
                    }
                }
            }
        }
    )
}





fun handleStartGame(navController: NavController, model: GameModel, ships: List<Ship>, gameBoard: List<Int>){
    model.saveShipToFirebase("battleId", ships, gameBoard)
    navController.navigate("GameScreen")
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
