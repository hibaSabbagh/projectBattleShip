package com.example.battleship_delin_hiba

data class Player (                         //data klass som representerar en spelare och namn en tom sträng som default
    val name: String = "",
    var status : String = ""
)

data class Battle(                            //data klass som representerar en battle och defaultvärden
    val player1Id: String = "",
    val player2Id: String = "",
    var gameState: GameState = GameState.Invite,
    var gameBoardP1: List<Int> = List(100) {0},
    var gameBoardP2: List<Int> = List(100) {0}
)



enum class  Orientation{
    HORIZONTAL,
    VERTICAL
}

data class Ship(
    val size: Int,
    var start : Int,
    var orientation: Orientation = Orientation.VERTICAL,

    )


enum class GameState{
    Invite,
    accepted,
    player1_turn,
    player2_turn,
    player1_win,
    player2_win,
    Cancelled,
    waiting_for_opponent
}

object BoardConstants {
    const val CELL_SIZE = 40
}