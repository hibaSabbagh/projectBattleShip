package com.example.battleship_delin_hiba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.battleship_delin_hiba.ui.theme.BattleshipDelinHibaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BattleshipDelinHibaTheme {
                BattleNav()
            }
        }
    }
}

