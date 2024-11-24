package com.example.battleship_delin_hiba

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

@SuppressLint("SuspiciousIndentation")                                   //  Första skärm som visas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, model: GameModel, sharedPreferences: SharedPreferences) {  //för firebase
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        model.localPlayerId.value = sharedPreferences.getString("playerId", null)
        if(model.localPlayerId.value != null){
            navController.navigate("Lobby")
        }
    }
    if(model.localPlayerId.value == null){
        var playerName by remember { mutableStateOf("") }
        var isJoining by remember { mutableStateOf(false) }                                                       //flagga för att kolla om användaren har tryckt på join lobby
        val playerInvalid = playerName.isEmpty() || players.any { it.value.name == playerName } || !(playerName.matches(Regex("^[a-zA-Z]*")))

        Scaffold(
            floatingActionButton = {  ExtendedFloatingActionButton(                  //för join lobby knappen
                onClick = {
                    isJoining = true                                                   //när knappen trycks på så ändras flaggan till true
                    if(!playerInvalid){
                       handleJoinGame(navController, model, playerName, sharedPreferences)              //anropar handleJoinGame om namnet är giltigt
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
                            isError = !isJoining && playerInvalid ,            //visa fel endast efter att användaren har tryckt på join lobby.
                            supportingText = {
                                if (!isJoining && playerInvalid) {             //visa fel om join lobby har tryckts men namnet är ogiltigt
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
}

@Composable
fun MyImage(){            //för battelship bilden, beskriver bilden
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.shippic),
            contentDescription = "main screen picture of the game",
            modifier = Modifier.padding(top = 100.dp).size(350.dp),
            contentScale = ContentScale.Fit
        )
    }
}

fun handleJoinGame(navController: NavController,model: GameModel, playerName:String, sharedPreferences: SharedPreferences){
    val newPlayer = Player(playerName, "Online")
    model.db.collection("players").add(newPlayer).addOnSuccessListener {
            documentRef-> val newPlayerId = documentRef.id
        sharedPreferences.edit().putString("playerId", newPlayerId).apply()
        model.localPlayerId.value = newPlayerId
        navController.navigate("Lobby")}.addOnFailureListener { error ->
        Log.e("Firebase", "Error adding document: $error")
    }
}
           //skapa en ny spelare med namn och status online
           //lägger till spelaren i databasen och sparar spelarens id i shared preferences
           //och sen navigerar till LobbyScreen




