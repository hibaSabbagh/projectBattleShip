package com.example.battleship_delin_hiba

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow

class GameModel: ViewModel(){
    val db = Firebase.firestore
    var localPlayerId = mutableStateOf<String?>(null)
    val playerMap = MutableStateFlow<Map<String, Player>>(emptyMap())
    val battleMap = MutableStateFlow<Map<String, Battle>>(emptyMap())

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
}