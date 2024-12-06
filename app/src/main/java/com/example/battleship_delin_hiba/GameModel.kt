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

    fun checkWin(board: List<Int>): Boolean {
        val playerHit = board.count { it == 2 }
        return playerHit == 13
    }

    fun handleCellClick(index: Int, BattleId: String?) {
        Log.d(
            "BattleScreen",
            "handleCellClick called with index: $index for ${BattleId.toString()}"
        )
        for (i in battleMap.value) {
            Log.d("BattleScreen", "handleCellClick called with BattleId: $i")
        }
        if (BattleId == null) return
        val battle = battleMap.value[localBattleId.value] ?: return
        val myTurn =
            battle.gameState == GameState.player1_turn && battle.player1Id == localPlayerId.value
                    || battle.gameState == GameState.player2_turn && battle.player2Id == localPlayerId.value
        if (!myTurn) return

        val (opponentBoardKey, opponentBoard, nextGameState) = if (localPlayerId.value == battle.player1Id) {
            Triple("gameBoardP2", battle.gameBoardP2.toMutableList(), GameState.player2_turn)
        } else Triple("gameBoardP1", battle.gameBoardP1.toMutableList(), GameState.player1_turn)

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
        val NewGameState = if (checkWin(opponentBoard)) {
            if (localPlayerId.value == battle.player1Id) {
                GameState.player1_win
            } else {
                GameState.player2_win
            }
        } else nextGameState

        db.collection("battles").document(BattleId).update(
            opponentBoardKey, opponentBoard,
            "gameState", NewGameState
        )
    }
}