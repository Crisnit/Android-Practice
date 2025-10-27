package com.example.androidpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.androidpractice.content.DetailsActivityScreen
import com.example.androidpractice.ui.theme.AndroidPracticeTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPracticeTheme {
                val model = intent.getStringExtra("CAR_MODEL") ?: ""
                DetailsActivityScreen(model = model, onBackClick = { finish() })
            }
        }
    }
}
