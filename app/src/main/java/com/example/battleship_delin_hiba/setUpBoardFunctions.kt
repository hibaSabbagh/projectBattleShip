package com.example.battleship_delin_hiba

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

enum class  Orientation{
    HORIZONTAL,
    VERTICAL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpBoardScreen(navController: NavController,model: GameModel) {
    val tiles = 10
    val boardDataChange = Array(tiles) { Array(tiles) { 0 } }
    for(i in boardDataChange.indices){
        for(j in boardDataChange[i].indices){
            boardDataChange[i][j] = 0
        }
    }
    boardDataChange[0][0] = 1

    boardDataChange[0][0] = 1

    boardDataChange[2][0] = 1
    boardDataChange[3][0] = 1
    boardDataChange[4][0] = 1
    boardDataChange[5][0] = 1

    boardDataChange[0][0] = 1
    boardDataChange[0][0] = 1
    boardDataChange[0][0] = 1

    boardDataChange[0][0] = 1
    boardDataChange[0][0] = 1

    boardDataChange[0][0] = 1
    boardDataChange[0][0] = 1

    class Ship(
        val size : Int,
        x: Int,
        y: Int,
        orientation : Orientation = Orientation.VERTICAL
    ){

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
    *
    *
    *
    *
    *
    *
    * */
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
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ){
                        Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("Battle") },
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = { Text("Start Game") }
            )
        },
        content = { padding ->
            Column (modifier = Modifier.padding(padding).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(tiles),
                    modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 50.dp).fillMaxWidth().height(400.dp)
                ){
                    itemsIndexed(boardDataChange.flatten()){ index, tileValue ->
                        val row = index / tiles
                        val column = index % tiles
                        Box(Modifier.fillMaxSize().background(
                            color = if (boardDataChange[row][column] == 0) {
                                        Color.White
                                } else {
                                        Color.LightGray
                                },
                            shape = RectangleShape
                        )
                            .size(30.dp)
                            .border(1.dp, Color.Black).clickable { boardDataChange[row][column] = 1 }
                        )
                    }
                }
            }
        }
    )
}