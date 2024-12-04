package com.example.battleship_delin_hiba


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow


class GameModel :
    ViewModel() {                              //hantera all data och koppla appen till Firebase Firestore
    val db = Firebase.firestore
    var localPlayerId = mutableStateOf<String?>(null)
    val playerMap = MutableStateFlow<Map<String, Player>>(emptyMap())
    val battleMap = MutableStateFlow<Map<String, Battle>>(emptyMap())
    var localBattleId = mutableStateOf<String?>(null)

    fun initGame() {
        db.collection("players").addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                val updatedPlayerMap = value.documents.associate { doc ->
                    doc.id to doc.toObject(Player::class.java)!!
                }
                playerMap.value = updatedPlayerMap
            }
        }
        db.collection("battles").addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                val updatedGameMap = value.documents.associate { doc ->
                    doc.id to doc.toObject(Battle::class.java)!!
                }
                battleMap.value = updatedGameMap
            }
        }
    }

    val _ships = mutableStateListOf(
        Ship(size = 4, start = 0, orientation = Orientation.HORIZONTAL),
        Ship(size = 3, start = 20, orientation = Orientation.HORIZONTAL),
        Ship(size = 2, start = 40, orientation = Orientation.HORIZONTAL),
        Ship(size = 2, start = 50, orientation = Orientation.HORIZONTAL),
        Ship(size = 1, start = 60, orientation = Orientation.HORIZONTAL),
        Ship(size = 1, start = 70, orientation = Orientation.HORIZONTAL)
    )

    fun placeShipInBoard(ships: List<Ship>): List<Int> {
        val board = MutableList(100) { 0 }
        for (ship in ships) {
            if (ship.orientation == Orientation.HORIZONTAL) {
                for (j in ship.start until ship.start + ship.size) {
                    board[j] = 1
                }
            } else if (ship.orientation == Orientation.VERTICAL) {
                var count = ship.start
                var shipSize = 0
                while (count < board.size && shipSize <= ship.size) {
                    board[count] = 1
                    count += 10
                    shipSize += 1
                }
            }
        }
        return board
    }
    fun changeShipOrientation(ship: Ship) {
        if (ship.orientation == Orientation.HORIZONTAL) {
            ship.orientation = Orientation.VERTICAL
        } else if (ship.orientation == Orientation.VERTICAL) {
            ship.orientation = Orientation.HORIZONTAL
        }
    }

    fun handleTilePress(index: Int, BattleId: String?) {
        if (BattleId != null) {
            val battle = battleMap.value[BattleId]
            if (battle != null) {
                val myTurn = battle.gameState == GameState.player1_turn && battle.player1Id == localPlayerId.value
                        || battle.gameState == GameState.player2_turn && battle.player2Id == localPlayerId.value

               if(localPlayerId.value == battle.player1Id){
                   if(myTurn){
                       val gameBoardP2 = battle.gameBoardP2.toMutableList()
                       if(gameBoardP2[index] == 0){
                           gameBoardP2[index] = -1
                       } else if(gameBoardP2[index] == 1){
                           gameBoardP2[index] = 2
                       }
                       db.collection("battles").document(BattleId).update("gameBoardP2", gameBoardP2)
                   }
               }
            }
        }
    }
}

// i * cols + j
/*
* fun getPosition(index: Int, boardSize: Int = 10): Pair<Int, Int> {
    val row = index / boardSize
    val column = index % boardSize
    return Pair(row, column)
}
* */


