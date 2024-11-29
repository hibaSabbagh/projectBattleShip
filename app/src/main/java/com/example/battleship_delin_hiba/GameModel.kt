package com.example.battleship_delin_hiba

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow


enum class  Orientation{
    HORIZONTAL,
    VERTICAL
}

class Ship(
    val size: Int,

    x : Int,
    y : Int,
    orientation: Orientation = Orientation.VERTICAL,

    ) {
    var X = mutableIntStateOf(x)   // on position change
    var Y = mutableIntStateOf(y)
    var orientation = mutableStateOf(orientation)
    var hits = mutableIntStateOf(0)
    fun isSunk(): Boolean {
        return hits.value == size
    }
    fun placement(): {

    }
}

// hantera logiken här

class GameModel: ViewModel(){                              //hantera all data och koppla appen till Firebase Firestore
    val db = Firebase.firestore
    var localPlayerId = mutableStateOf<String?>(null)
    val playerMap = MutableStateFlow<Map<String, Player>>(emptyMap())
    val battleMap = MutableStateFlow<Map<String, Battle>>(emptyMap())
    var localBattleId = mutableStateOf<String?>(null)
    var localBoardId = mutableStateOf<String?>(null)

    private val _ships = listOf(
        Ship(size = 4, 2, 0, Orientation.VERTICAL),
        Ship(size = 3, 1, 9, Orientation.VERTICAL),
        Ship(size = 2, 0, 4, Orientation.HORIZONTAL),
        Ship(size = 2, 9, 3, Orientation.HORIZONTAL),
        Ship(size = 1, 4, 4, Orientation.VERTICAL),
        Ship(size = 1, 0, 0, Orientation.VERTICAL)
    )

    fun initGame(){
        db.collection("players").addSnapshotListener{ value, error ->
            if(error != null){
                return@addSnapshotListener
            }
            if(value != null){
                val updatedPlayerMap = value.documents.associate {doc ->
                    doc.id to doc.toObject(Player::class.java)!!
                }
                playerMap.value = updatedPlayerMap
            }
        }
        db.collection("battles").addSnapshotListener{ value, error ->
            if(error != null){
                return@addSnapshotListener
            }
            if(value != null){
                val updatedGameMap = value.documents.associate {doc ->
                    doc.id to doc.toObject(Battle::class.java)!!
                }
                battleMap.value = updatedGameMap
            }
        }
    }


    fun checkWinner(gameBoardP1: List<Int>, gameBoardP2: List<Int>): Int {
        val player1Won = !gameBoardP2.contains(1)
        val player2Won = !gameBoardP1.contains(1)
        return when {
            player1Won -> 1
            player2Won -> 2
            else -> 0
        }
    }



//    fun checkGameState(gameId: String) {
//        val game = battleMap.value[gameId] ?: return
//        val currentPlayer = game.player1Id.value ?: return
//
//        val playerShip = if (game.player1Id == currentPlayer) {
//            game.player1Id.map { it.toShip() }
//        } else {
//            game.player2Id.map { it.toShip() }
//        }
//
//        val opponentShip = if (game.player1Id == currentPlayer) {
//            game.player2Id.map { it.toShip() }
//        } else {
//            game.player1Id.map { it.toShip() }
//        }
//        val winner = checkWinner(playerShip, opponentShip)
//        val newGameState = when(winner) {
//            1 -> "player1_won"
//            2 -> "player2_won"
//            else -> if (Battle.gamestate == "player1_turn") "player2_turn" else "player1_turn"
//        }
//
//        db.collection("battles").document(gameId)
//            .update(
//                mapOf(
//                    "gamestate" to newGameState,
//                    "player1Id" to game.player1Id,
//                    "player2Id" to game.player2Id
//                )
//            )
//    }




    fun handleTilePress(gameId: String, x: Float, y: Float){


    }
}