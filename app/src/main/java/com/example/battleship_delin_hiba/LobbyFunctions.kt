package com.example.battleship_delin_hiba

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LobbyScreen(navController: NavController, playerList: MutableList<Player>){
    /* each player has a button(challenge) that takes you to the battle screen
    * */


}


@Composable
fun PlayerListLoop(playerList: MutableList<Player>){
    for (player in playerList){

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