package com.example.battleship_delin_hiba

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.UUID

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, playerList: MutableList<Player>) {
    var playerName by remember { mutableStateOf("") }
    var isJoining by remember { mutableStateOf(false) }                                //flagga för att kolla om användaren har tryckt på join lobby
    val playerValidation = playerName.isEmpty() || playerList.any { it.name == playerName } || !(playerName.matches(Regex("^[a-zA-Z]*")))

    Scaffold(
        floatingActionButton = {  ExtendedFloatingActionButton(
            onClick = {
                isJoining = true                                     //när knappen trycks på så ändras flaggan till true
                if(!playerValidation){
                    handleJoinGame(navController,playerList,playerName)
                }
            },
            modifier = Modifier.padding(16.dp),
            shape = CircleShape,
            containerColor = Color(0xFFD3368E),
            contentColor = Color.Black,
            content = {Text("Join Lobby")}
        )}
    ) { padding ->
        MyImage()
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).background(Color(0xFFD986AC),  RoundedCornerShape(4.dp))
                .padding(16.dp) ) {
                    Column {
                        TextField(
                            value = playerName,
                            onValueChange = { playerName = it },
                            label = { Text("Name") },
                            isError = !isJoining && playerValidation ,            //visa fel endast efter att användaren har tryckt på join lobby.
                            supportingText = {
                                if (!isJoining && playerValidation) {             //visa fel om join lobby har tryckts men namnet är ogiltigt
                                    Text(
                                        text = "Invalid name",
                                        color = MaterialTheme.colorScheme.error
                                    )
                            }},
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor =  Color.Transparent,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black),
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
           }
    }
}

@Composable
fun MyImage(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.shippic),
            contentDescription = "main screen picture of the game",             //beskriver bilden
            modifier = Modifier.padding(top = 100.dp).size(350.dp),
            contentScale = ContentScale.Fit
        )
    }
}


fun handleJoinGame(navController: NavController, playerList: MutableList<Player>, playerName: String){
    val player = Player(playerName, UUID.randomUUID().toString(),"online")
    if(!playerName.isEmpty() && !(playerList.any { it.name == playerName }) && (playerName.matches(Regex("^[a-zA-Z]*")))) {
        playerList.add(player)
        navController.navigate("Lobby")
    }
}

