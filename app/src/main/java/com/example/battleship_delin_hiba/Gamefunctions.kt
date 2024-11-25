package com.example.battleship_delin_hiba

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.AccountCircle


                                                                     // Fjärde skärm som visas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(navController: NavController, model: GameModel){
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

                },
                navigationIcon = {
                    IconButton(
                        onClick = { handleLeaveGame(navController, model) }       //kanske till main
                    ) {
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
                onClick = { handleLeaveGame(navController, model) },            //kanske till main
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = { Text("Leave Game") }
            )
        }
    ) { padding ->
        Column( modifier = Modifier
            .padding()
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top){
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(                                               //ikon för player1
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "person",
                        modifier = Modifier.size(40.dp)
                    )
                    Text( text = "player 1")
                }
                Spacer(modifier = Modifier.width(30.dp))             //space mellan player1 och vs.
                Text(
                    text = "vs.",
                    modifier = Modifier.padding(horizontal = 16.dp).size(40.dp),
                )
                Spacer(modifier = Modifier.width(20.dp))            //space mellan vs. och player2
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(                                                 //ikon för player2
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "person",
                        modifier = Modifier.size(40.dp)
                    )
                    Text( text = "player 2")
                }
            }
            Spacer(
                modifier = Modifier.height(50.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(tiles),
                modifier = Modifier
                    .padding(start = 50.dp,end = 50.dp).
                    fillMaxWidth().height(400.dp)
            ) {
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(tiles),
                modifier = Modifier.size(width = 200.dp, height = 200.dp).padding(bottom = 10.dp)
            ) {
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

fun handleLeaveGame(navController: NavController, model: GameModel){
   val currentBattleId = model.localBattleId.value!!
    if( currentBattleId != null) {
        model.db.collection("battles").document(currentBattleId).delete().addOnSuccessListener {
            model.localBattleId.value = null
            model.localBoardId.value = null
            navController.navigate("Lobby") {
                popUpTo("Battle") { inclusive = true }
            }
        }.addOnFailureListener {
            Log.e("BattleScreen", "Failed to delete battle", it)
        }
    }else {
        Log.w("BattleScreen", "Current battle ID is null")
//        model.localBattleId.value = null
//        model.localBoardId.value = null
        navController.navigate("Lobby") {
            popUpTo("Battle") { inclusive = true }
        }
    }
}