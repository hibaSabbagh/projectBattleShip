package com.example.battleship_delin_hiba

import android.R.attr.padding
import android.R.attr.title
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, playerList: MutableList<Player>, battlesList: MutableList<Battle>) {
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
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                        Spacer(modifier = Modifier.padding(100.dp))             //hur mycket utrymme mellan row och column
                        Text(text = "Online")
                        Spacer(modifier = Modifier.padding(5.dp))               //space mellan gröna cirkel och online
                        Box(
                            modifier = Modifier.size(15.dp).clip(CircleShape)
                                .background(Color.Green)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { handleLeaveLobby(navController, playerList)},   // we can change this when when we connect to the server
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {Text("Leave Lobby")}

            )
       },
        content = { padding -> PlayerListLoop(padding, playerList, navController)},
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text (text = "${battlesList.size} active games")
                        Icon( painter = painterResource(id = R.drawable.directions_boat),
                            contentDescription = "boat")
                    }

                }

            )

       }
    )

}




@Composable
fun PlayerListLoop( padding : PaddingValues, playerList: MutableList<Player>, navController: NavController){
    if (playerList.isEmpty()){
        Text(text = "No players online")
    }else {
        LazyColumn(modifier = Modifier.padding(padding))
        {
            items(playerList) { player ->
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = player.name) },
                    trailingContent = {
                        Button(
                            onClick = { navController.navigate("Battle") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD3368E)),
                           ){ Text(text = "Challenge") }
                    }

                )
            }
        }
    }
}



//@Composable
//fun  ChallengeButton(player: Player){}

fun handleLeaveLobby(navController: NavController, playerList: MutableList<Player>){
    navController.navigate("Main")
    playerList.removeLast()
}

@Composable
fun ChallengePopup(){

}