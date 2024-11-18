package com.example.battleship_delin_hiba

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.UUID

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, playerList: MutableList<Player>) {
    var playerName by remember { mutableStateOf("") }
    var isJoining by remember { mutableStateOf(false) }         //flagga för att kolla om användaren har tryckt på join lobby
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
                            isError = !isJoining && playerValidation ,            //visa fel endast efter att användaren har tryckt på join lobby
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




//@Composable
//fun JoinGameButton(onClick: ()->Unit){
//        Button(
//            onClick = onClick,
//            shape = CircleShape,
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB60D51)),
//            modifier = Modifier.width(200.dp).height(100.dp).padding(16.dp).offset(x =160.dp,y= 290.dp)
//        ){
//            Text(text = "Join Game")
//        }
//}
