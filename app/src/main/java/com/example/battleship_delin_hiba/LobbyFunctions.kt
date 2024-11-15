package com.example.battleship_delin_hiba

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(navController: NavController, playerList: MutableList<Player>) {
    printPlayers(playerList)
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
                onClick = { navController.navigate("Main")
                           playerList.remove(Player("","",""))},
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD3368E),
                contentColor = Color.Black,
                content = {Text("Leave Lobby")}
            ) },
        content = { padding -> PlayerListLoop(padding,playerList,navController)}
    )

}




@Composable
fun PlayerListLoop(padding: PaddingValues,playerList: MutableList<Player>, navController: NavController){
    if (playerList.isEmpty()){
        Text(text = "No players online")
    }else {
        LazyColumn(modifier = Modifier.padding(padding))
        {
            items(playerList) { player ->
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "person"
                        )
                    },
                    headlineContent = { Text(text = player.name) },
                    trailingContent = {
                        Button(onClick = { navController.navigate("Battle") }) {
                            Text(
                                "Challenge"
                            )
                        }
                    }

                )
            }
        }
    }
}

@Composable
fun printPlayers(playerList: MutableList<Player>){
    for (player in playerList){
        println(player.name)
    }
}

@Composable
fun  ChallengeButton(player: Player){}

@Composable
fun ActiveGames (player: Player, battlesMap: MutableMap<String, Battle>){

}

@Composable
fun ChallengePopup(){

}