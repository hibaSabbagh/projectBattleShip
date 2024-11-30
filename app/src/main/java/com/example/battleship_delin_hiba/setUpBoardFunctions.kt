package com.example.battleship_delin_hiba



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController, model: GameModel) {
    val gameCellGrid = remember { model.gameCellGrid }
    val ships = remember { model._ships }

//    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
//    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
//    val gameBoard = remember { mutableStateListOf(*List(100) { 0 }.toTypedArray()) }
//    val ships = remember {
//        mutableStateListOf(
//            Ship(size = 4, start = 0),
//            Ship(size = 3, start = 20),
//            Ship(size = 2, start = 40),
//            Ship(size = 2, start = 50),
//            Ship(size = 1, start = 60),
//            Ship(size = 1, start = 70)
//        )
//    }

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
                    onClick = { model.saveShipToFirebase("battleId", navController )
                              navController.navigate("Battle")},                           // handel start game
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
                    items(gameCellGrid.size){ index ->
                        val cell = gameCellGrid[index]
                        Box (
                            modifier = Modifier
                                .size(40.dp)
                                .background(if(cell.empty.value){Color.White}
                                    else {Color.Gray})
                                .border(1.dp, Color.Black).
                                    pointerInput(cell){
                                        detectDragGestures{
                                            _, dragAmount ->
                                            val ship = ships.firstOrNull{ it.start == index}
                                            ship?.let{
                                                val deltaX = dragAmount.x.toInt()/40
                                                val deltaY = dragAmount.y.toInt()/40
                                                val newX = (it.start % 10) + deltaX
                                                val newY = (it.start / 10) + deltaY
                                                val newPosition = newY * 10 + newX

                                                model.moveShip(it, newPosition)

                                            }
                                        }
                                    }

                                .clickable{
                                    val selectedShip = ships.firstOrNull{ it.start == index}
                                    selectedShip?.let{
                                        model.toggleShipOrientation(it)
                                    }
                                }

                        )
                    }
                }
            }
        }
    )
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
