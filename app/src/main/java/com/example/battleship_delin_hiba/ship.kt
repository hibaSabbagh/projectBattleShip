package com.example.battleship_delin_hiba

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset

//class Ship(
//val size: Int,
//var start : Int,
//var orientation: Orientation = Orientation.VERTICAL,
//) {
//    fun changeShipOrientation(shipToChange: Ship, ships: List<Ship>): List<Ship> {
//        return ships.map { ship ->
//            if (shipToChange == ship) {
//                ship(
//                    orientation = if (ship.orientation == Orientation.HORIZONTAL)
//                        Orientation.VERTICAL
//                    else
//                        Orientation.HORIZONTAL )
//            }else ship
//        }
//    }
//}



enum class  Orientation{
    HORIZONTAL,
    VERTICAL
}

data class Ship(
    val size: Int,
    var start : Int,
    var orientation: Orientation = Orientation.VERTICAL,
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

var _ships = mutableStateListOf(
    Ship(size = 4, start = 0, orientation = Orientation.HORIZONTAL),
    Ship(size = 3, start = 20, orientation = Orientation.HORIZONTAL),
    Ship(size = 2, start = 40, orientation = Orientation.HORIZONTAL),
    Ship(size = 2, start = 50, orientation = Orientation.HORIZONTAL),
    Ship(size = 1, start = 60, orientation = Orientation.HORIZONTAL),
    Ship(size = 1, start = 70, orientation = Orientation.HORIZONTAL)
)
fun updateBoardWithShip(board: List<Int>, ship: Ship, newStart:Int): MutableList<Int> {
    val newBoard = board.toMutableList()
    val oldPositions = getShipPositions(ship)
    val newPositions = if(ship.orientation == Orientation.HORIZONTAL){
        (newStart until newStart + ship.size).toList()
    }else {
        (0 until ship.size).map{newStart + it*10}
    }
    oldPositions.forEach{newBoard[it] = 0}
    newPositions.forEach{newBoard[it] = 1}
    return newBoard
}
fun isValidPosition(start: Int, size: Int, orientation: Orientation, boardSize: Int): Boolean {
    val rowSize = 10
    return (if  (orientation == Orientation.HORIZONTAL) {
        val end = start+size-1
        end < boardSize && (start / rowSize) == (end / rowSize)
    }else {
        val end = start +(size-1)*rowSize
        end < boardSize
    })

}
fun getShipPositions(ship: Ship): List<Int>{
    return if ( ship.orientation == Orientation.HORIZONTAL)
    {
        (ship.start until ship.start + ship.size).toList()
    }else { (0 until ship.size).map {ship.start +it*10 }

    }
}


fun findShipAtPosition(index: Int, ships: List<Ship>): Ship?{
    return ships.find { ship ->
        val positions = getShipPositions(ship)
        index in positions
    }
}


fun calculateNewStartPosition(ship: Ship, dragAmount:Offset, boardSize: Int): Int {
    val rowSize = 10
    val column = ship.start % rowSize
    val row = ship.start / rowSize
    val targetColumn = (column + (dragAmount.x / 40).toInt()).coerceIn(0,rowSize-1)
    val targetRow = (row + (dragAmount.y / 40).toInt()).coerceIn(0,boardSize/ rowSize-1)
    return targetRow * rowSize + targetColumn
}