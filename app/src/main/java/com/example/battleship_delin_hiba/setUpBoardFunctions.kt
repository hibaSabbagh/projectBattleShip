package com.example.battleship_delin_hiba

import android.R.attr.onClick
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
    val firebaseRepository = FirebaseRepository()                          //****
    val tiles = 10
    val boardDataChange = Array(tiles) { Array(tiles) { 0 } }

    fun saveBoard() {
        firebaseRepository.saveBoardToFirestore(boardDataChange, "game123") { success ->
            if (success) {
                println("Board saved successfully!")                                    //*** exempel kan ändras
            } else {
                println("Failed to save board.")                                       //*** exempel kan ändras
            }
        }
    }

    fun loadBoard() {                                                                    // Funktion för att ladda brädet
        firebaseRepository.loadBoardFromFirestore("game123") { loadedBoard ->
            if (loadedBoard != null) {
                for (i in loadedBoard.indices) {
                    for (j in loadedBoard[i].indices) {
                        boardDataChange[i][j] = loadedBoard[i][j]                        // Uppdatera brädet
                    }
                }
                println("Board loaded successfully!")
            } else {
                println("Failed to load board.")
            }
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
                            navController.navigate("Lobby")
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
            Column {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("Battle") },
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD3368E),
                    contentColor = Color.Black,
                    content = { Text("Start Game") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExtendedFloatingActionButton(
                    onClick = { saveBoard() },                  //anropar "spara funktionen"
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD3368E),
                    contentColor = Color.Black,
                    content = { Text("Save Board") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExtendedFloatingActionButton(
                    onClick = { loadBoard() },                 //anropar "ladda funktionen"
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD3368E),
                    contentColor = Color.Black,
                    content = { Text("Load Board") }
                )
            }
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
                        Box( Modifier.fillMaxSize().background(
                            color = if (boardDataChange[row][column] == 0) {
                                        Color.White
                                } else {
                                        Color.LightGray
                                },
                            shape = RectangleShape
                        )
                            .size(30.dp)
                            .border(1.dp, Color.Black)
                        )
                    }
                }
            }
        }
    )
}
