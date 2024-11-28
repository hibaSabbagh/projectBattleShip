package com.example.battleship_delin_hiba

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue

class FirebaseRepository {             //denna ska anropas i SetUpBoard
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val realtimeDb = FirebaseDatabase.getInstance().reference


    fun saveBoardToFirestore(board: Array<Array<Int>>, documentId: String, onComplete: (Boolean) -> Unit) {                       // Spara brädet i Firestore
        val boardForFirebase = board.map { row -> row.toList() }
        val data = hashMapOf("board" to boardForFirebase)

        firestoreDb.collection("boards").document(documentId)
            .set(data)
            .addOnSuccessListener {
                println("Board successfully saved!")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Error saving board: ${e.message}")
                onComplete(false)
            }
    }


    fun loadBoardFromFirestore(documentId: String, onResult: (Array<Array<Int>>?) -> Unit) {                // Läs brädet från Firestore
        firestoreDb.collection("boards").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val boardFromFirebase = document["board"] as? List<List<Long>>
                    if (boardFromFirebase != null) {
                        val board = Array(boardFromFirebase.size) { row ->
                            Array(boardFromFirebase[row].size) { col ->
                                boardFromFirebase[row][col].toInt()

                            }
                        }
                        onResult(board)
                    } else {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                println("Error loading board: ${e.message}")
                onResult(null)
            }
    }

    fun saveBoardToRealtimeDatabase(board: Array<Array<Int>>, gameId: String, onComplete: (Boolean) -> Unit) {                 // Spara brädet i Realtime Database
        val boardForFirebase = board.map { row -> row.toList() }

        realtimeDb.child("games").child(gameId).setValue(boardForFirebase)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Board successfully saved!")
                    onComplete(true)
                } else {
                    println("Error saving board: ${task.exception?.message}")
                    onComplete(false)
                }
            }
    }


    fun loadBoardFromRealtimeDatabase(gameId: String, onResult: (Array<Array<Int>>?) -> Unit) {            // Läs brädet från Realtime Database
        realtimeDb.child("games").child(gameId).get()
            .addOnSuccessListener { snapshot ->
                val boardFromFirebase = snapshot.getValue<List<List<Long>>>()
                if (boardFromFirebase != null) {
                    val board = Array(boardFromFirebase.size) { row ->
                        Array(boardFromFirebase[row].size) { col ->
                            boardFromFirebase[row][col].toInt()
                        }
                    }
                    onResult(board)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                println("Error loading board: ${e.message}")
                onResult(null)
            }
    }
}