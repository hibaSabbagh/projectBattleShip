package com.example.battleship_delin_hiba



import android.icu.text.Transliterator
import androidx.compose.foundation.background
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
fun SetUpBoardScreen(navController: NavController,model: GameModel) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val battles by model.battleMap.asStateFlow().collectAsStateWithLifecycle()
    val shipPositions = remember { mutableStateOf(Pair(0,0)) }

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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    modifier = Modifier.fillMaxSize()
                ){
                    items(100){ index ->
                        val row = index / 10
                        val column = index % 10
                        val position = row to column
                        Box (
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White)

                        )

                    }
                }
                ship( shipPositions.value, onPositionChange = {newPosition ->
                    shipPositions.value = newPosition})
            }
        }
    )
}





@Composable
fun ship(
    position: Pair<Int, Int>,
    onPositionChange: (Pair<Int, Int>) -> Unit
){
    Box (
        modifier = Modifier
            .size(50.dp)
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onPositionChange( position.first + dragAmount.x.toInt() / 50 to  position.second + dragAmount.y.toInt() / 50)
                }
            }
    )
}

fun handleStartGame(navController: NavController, model: GameModel){
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
