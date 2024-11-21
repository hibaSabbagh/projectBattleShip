package com.example.battleship_delin_hiba

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(navController: NavController){
    /*
    * 2D board that has all the tiles connected to
    * 10x10 action buttons that see if there's a ship there
    * each cell will  have either one or zero value (ship or no ship) when clicked the
    * value will be multiplied by 2 and if the result is 2 then it hit ship if it is zero then it missed
    * missed will have x on them hit will have a red dot on them
    * */
    val tiles = 10
    val boardData = Array(tiles) { Array(tiles) { 0 } }
    for(i in boardData.indices){
        for(j in boardData[i].indices){
            boardData[i][j] = 0    //(0..1).random()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("Lobby") },
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = { Text("Leave Game") }
            )
        }

    ) { padding ->
        Column( modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(tiles),
                modifier = Modifier
                    .padding(start = 50.dp,end = 50.dp).
                    fillMaxWidth().height(400.dp)
            )
            {
                itemsIndexed(boardData.flatten()) { index, tileValue ->
                    val row = index / tiles
                    val column = index % tiles
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                color = if (boardData[row][column] == 0) {
                                    Color.White
                                } else {
                                    Color.Gray
                                },
                                shape = RectangleShape
                            )
                            .size(30.dp)
                            .border(1.dp, Color.Black).clickable{ boardData[row][column] = 1 }


                        )
                }
            }
            //Spacer(modifier = Modifier.height(2.dp).width(2.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(tiles),
                modifier = Modifier
                    .padding(start = 0.dp, end = 0.dp).
                    size(width = 200.dp, height = 200.dp)
            )
            {
                itemsIndexed(boardData.flatten()) { index, tileValue ->
                    val row = index / tiles
                    val column = index % tiles
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                color = if (boardData[row][column] == 0) {
                                    Color.White
                                } else {
                                    Color.Red
                                },
                                shape = RectangleShape
                            )
                            .size(15.dp)
                            .border(1.dp, Color.Black)
                    )
                }
            }
        }
    }
}




//@Composable
//fun checkIfShip( tileValue: Int) : Boolean{
//    return tileValue * 2 == 2
//}


