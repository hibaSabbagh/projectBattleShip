package com.example.battleship_delin_hiba


import android.util.Log
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

    var _ships = mutableStateListOf(
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


    fun changeShipOrientation(shipToChange: Ship, ships: List<Ship>): List<Ship> {
        return ships.map { ship ->
            if (shipToChange == ship) {
                ship.copy(
                    orientation = if (ship.orientation == Orientation.HORIZONTAL)
                        Orientation.VERTICAL
                     else
                        Orientation.HORIZONTAL )
            }else ship
        }
    }


    fun checkWin(board: List<Int>): Boolean {
        val playerHit = board.count { it == 2 }
        if (playerHit == 13) {
            return true
        }
        return false
    }


    fun handleTilePress(index: Int, BattleId: String?) {
        Log.d("BattleScreen", "handleTilePress called with index: $index for ${BattleId.toString()}")
        for(i in battleMap.value){
            Log.d("BattleScreen", "handleTilePress called with BattleId: $i")
        }
        if (BattleId != null) {
            val battle = battleMap.value[localBattleId.value]
            if (battle != null) {
                Log.d("BattleScreen", "handleTilePress called with BattleId: $BattleId")
                val myTurn =
                    battle.gameState == GameState.player1_turn && battle.player1Id == localPlayerId.value
                            || battle.gameState == GameState.player2_turn && battle.player2Id == localPlayerId.value


                val opponentBoardKey: String
                val opponentBoard: MutableList<Int>
                val nextGameState: GameState
                if (localPlayerId.value == battle.player1Id) {
                    opponentBoardKey = "gameBoardP2"
                    opponentBoard = battle.gameBoardP2.toMutableList()
                    nextGameState = GameState.player2_turn
                } else {
                    opponentBoardKey = "gameBoardP1"
                    opponentBoard = battle.gameBoardP1.toMutableList()
                    nextGameState = GameState.player1_turn
                }
                when (opponentBoard[index]) {
                    0 -> {
                        opponentBoard[index] = -1 // Sätt värdet till -1
                        Log.d("BattleScreen", "Tile at index $index set to -1 (blue)")
                    }
                    1 -> {
                        opponentBoard[index] = 2 // Exempel: ändra 1 till 2
                        Log.d("BattleScreen", "Tile at index $index set to 2")
                    }
                    else -> {
                        Log.d("BattleScreen", "Tile at index $index is not valid for change")
                        return
                    }
                }

                db.collection("battles").document(BattleId).update(opponentBoardKey, opponentBoard)

                if (checkWin(opponentBoard)) {
                    if (localPlayerId.value == battle.player1Id) {
                        db.collection("battles").document(BattleId).update("gameState", GameState.player1_win)
                    } else if (localPlayerId.value == battle.player2Id) {
                        db.collection("battles").document(BattleId).update("gameState", GameState.player2_win)
                    }
                } else {
                    db.collection("battles").document(BattleId).update("gameState", nextGameState)
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


