package com.example.androidpractice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.androidpractice.content.DetailsActivityScreen
import com.example.androidpractice.content.ItemData
import com.example.androidpractice.ui.theme.AndroidPracticeTheme

class DetailsActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val car = ItemData(
                intent.getStringExtra("CAR_MANUFACTURER").toString(),
                intent.getStringExtra("CAR_MODEL").toString(),
                intent.getStringExtra("CAR_YEARS").toString(),
                intent.getStringExtra("CAR_CLASS").toString(),
                intent.getStringExtra("CAR_POWERTRAIN").toString(),
                intent.getStringExtra("CAR_DESCRIPTION").toString(),
                intent.getStringExtra("CAR_IMAGE_PATH").toString()
            )
            DetailsActivityScreen(car) { finish() }
        }
    }
}
