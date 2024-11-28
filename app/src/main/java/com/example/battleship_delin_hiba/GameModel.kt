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

    x : Float,
    y : Float,
    orientation: Orientation = Orientation.VERTICAL,

    ) {
    var x = mutableFloatStateOf(x)
    var y = mutableFloatStateOf(y)
    var orientation = mutableStateOf(orientation)
    var hits = mutableIntStateOf(0)
    fun isSunk(): Boolean {
        return hits.value == size
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
        Ship(size = 4, -585.5292f, 1024.0963f, Orientation.VERTICAL),
        Ship(size = 3, -365.75513f, 1007.4595f, Orientation.VERTICAL),
        Ship(size = 2, -163.72134f, 973.02344f, Orientation.VERTICAL),
        Ship(size = 2, 74.60945f, 980.7355f, Orientation.VERTICAL),
        Ship(size = 1, 336.9688f, 1004.18533f, Orientation.VERTICAL),
        Ship(size = 1, 573.9590f, 992.188233f, Orientation.VERTICAL)
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

    fun handleLeaveGame(){}

    fun handleTilePress(x: Int, y: Int){

    }
}