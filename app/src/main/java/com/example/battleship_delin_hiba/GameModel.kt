package com.example.battleship_delin_hiba

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow


enum class  Orientation{
    HORIZONTAL,
    VERTICAL
}

 data class Ship(
    val size: Int,
    var start : Int,
    var orientation: Orientation = Orientation.VERTICAL,

    )

data class Cell(
    val position: Int,
    var empty: MutableState<Boolean> = mutableStateOf(true),
    var occupideByShip: Ship? = null
)

class GameModel: ViewModel(){                              //hantera all data och koppla appen till Firebase Firestore
    val db = Firebase.firestore
    var localPlayerId = mutableStateOf<String?>(null)
    val playerMap = MutableStateFlow<Map<String, Player>>(emptyMap())
    val battleMap = MutableStateFlow<Map<String, Battle>>(emptyMap())
    var localBattleId = mutableStateOf<String?>(null)
    var localBoardId = mutableStateOf<String?>(null)

    var gameCellGrid = mutableListOf<Cell>()
    public val _ships = mutableStateListOf(
        Ship(size = 4, start = 0, orientation = Orientation.VERTICAL),
        Ship(size = 3, start = 20, orientation = Orientation.VERTICAL),
        Ship(size = 2, start = 40, orientation = Orientation.HORIZONTAL),
        Ship(size = 2, start = 50, orientation = Orientation.HORIZONTAL),
        Ship(size = 1, start = 60, orientation = Orientation.VERTICAL),
        Ship(size = 1, start = 70, orientation = Orientation.VERTICAL)
    )



    init {
        for(i in 0 until 100){
            gameCellGrid.add(Cell(position = i))
        }
    }




    fun updateShipPosition(ship: Ship){
        gameCellGrid.forEach {
            it.empty.value = true
            it.occupideByShip = null
        }
        for(length in 0 until ship.size){
            val x = ship.start % 10
            val y = ship.start / 10

            val index = if(ship.orientation == Orientation.HORIZONTAL){
                x + length + (y * 10)
            } else {
                x + (y + length) * 10
            }
            if(index in gameCellGrid.indices){
                gameCellGrid[index].empty.value = false
                gameCellGrid[index].occupideByShip = ship
            }
        }
    }


    fun toggleShipOrientation(ship: Ship){
        ship.orientation = if(ship.orientation == Orientation.HORIZONTAL){
            Orientation.VERTICAL
        } else {
            Orientation.HORIZONTAL
        }
        updateShipPosition(ship)
    }


    fun moveShip(ship: Ship, newPosition: Int): Boolean {
        val newEndPosition = if(ship.orientation == Orientation.HORIZONTAL){
            newPosition + ship.size - 1
        } else {
            newPosition + (ship.size - 1) * 10
        }
        if(newEndPosition > 99) return false

        ship.start = newPosition
        updateShipPosition(ship)
        return true
    }


    fun checkWinner(): Int {
        val allCellsFilledByPlayer1 = gameCellGrid.all { it.empty.value == false }
        return when {
            allCellsFilledByPlayer1 -> 1
            else -> 0
        }
    }



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


//    fun checkWinner(gameBoardP1: List<Int>, gameBoardP2: List<Int>): Int {
//        val player1Won = !gameBoardP2.contains(1)
//        val player2Won = !gameBoardP1.contains(1)
//        return when {
//            player1Won -> 1
//            player2Won -> 2
//            else -> 0
//        }
//    }

    fun handleTilePress(gameId: String, x: Float, y: Float){}

//
//    fun moveShip(ship : Ship, newPosition: Int, gameBoard: List<Int>): Boolean {
//        val endPosition = if (ship.orientation == Orientation.HORIZONTAL){
//            newPosition + ship.size - 1
//        } else {
//            newPosition + (ship.size -1)* 10
//        }
//
//        if(endPosition > 99) return false
//
//        for (i in 0 until ship.size){
//            val index = if (ship.orientation == Orientation.HORIZONTAL){
//                newPosition +i
//            } else {
//                newPosition + i * 10
//            }
//
//            if( index >= gameBoard.size || gameBoard[index] != 0) return false
//        }
//
//        ship.start = newPosition
//        return true
//    }


    fun rotateShip(ship : Ship, gameBoard: List<Int>): Boolean {
        val newOrientation = if (ship.orientation == Orientation.HORIZONTAL){
            Orientation.VERTICAL
        } else {
            Orientation.HORIZONTAL
        }
        val canRotate = moveShip(ship, ship.start)
        if(canRotate){
            ship.orientation = newOrientation
        }
        return canRotate
    }


    fun isCollisionFree(ship: Ship, gameBoard: List<Int>): Boolean {
        val neighbors = listOf(-10 - 1, -10, -10 + 1, -1, 1, 10 - 1, 10, 10 + 1)

        for (i in 0 until ship.size) {
            val index = if (ship.orientation == Orientation.HORIZONTAL) {
                ship.start + i
            } else {
                ship.start + i * 10
            }
            for (neighbor in neighbors) {
                val neighborIndex = index + neighbor
                if (neighborIndex in gameBoard.indices && gameBoard[neighborIndex] != 0){
                    return false
                }
            }
        }
        return true
    }



    fun saveShipToFirebase(BattleId: String, navController: NavController){
        val shipsData = _ships.map { ship -> mapOf(
            "size" to ship.size,
            "start" to ship.start,
            "orientation" to ship.orientation.toString()
            )
        }

        db.collection("battles").document(BattleId).update("player1Ships", shipsData)
            .addOnSuccessListener {
                println("ships saved")
                navController.navigate("Battle")
            }
            .addOnFailureListener{ error ->
                println("ships not saved")
            }
    }
}