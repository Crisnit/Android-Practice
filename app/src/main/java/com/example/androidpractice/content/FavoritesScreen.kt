package com.example.androidpractice.content
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidpractice.di.AppModule
import com.example.androidpractice.presentation.ui.UiState
import com.example.androidpractice.presentation.viewmodel.FavoritesViewModel
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val db = AppModule.provideDatabase(context)
    val repo = AppModule.provideFavoritesRepository(db)
    val viewModel = viewModel<FavoritesViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return FavoritesViewModel(AppModule.provideGetFavoritesUseCase(repo)) as T
            }
        }
    )
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }
    AndroidPracticeTheme {
        when (state) {
            is UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${state.message}")
                }
            }
            is UiState.Success -> {
                val itemsArray = state.data
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(itemsArray) { item ->
                        val context = LocalContext.current
                        Button(
                            onClick = { /* Optionally navigate to details */ },
                            modifier = Modifier.fillMaxWidth().height(80.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF9393AB)),
                            shape = RectangleShape,
                        ) {
                            val carAsset = context.assets
                            val img = BitmapFactory.decodeStream(carAsset.open(item.imagePath))
                            Image(
                                bitmap = img.asImageBitmap(),
                                modifier = Modifier.width(100.dp),
                                contentDescription = "Car image"
                            )
                            Text(
                                modifier = Modifier
                                    .padding(0.dp)
                                    .fillMaxWidth(),
                                text = "${item.manufacturer} ${item.model}",
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}