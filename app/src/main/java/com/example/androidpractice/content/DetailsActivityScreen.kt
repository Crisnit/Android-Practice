package com.example.androidpractice.content
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidpractice.di.AppModule
import com.example.androidpractice.presentation.ui.UiState
import com.example.androidpractice.presentation.viewmodel.CarDetailsViewModel
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsActivityScreen(
    model: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = viewModel<CarDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val db = AppModule.provideDatabase(context)
                val favoritesRepo = AppModule.provideFavoritesRepository(db)
                return CarDetailsViewModel(
                    AppModule.getCarUseCase,
                    AppModule.provideAddFavoriteUseCase(favoritesRepo),
                    AppModule.provideRemoveFavoriteUseCase(favoritesRepo),
                    AppModule.provideGetFavoritesUseCase(favoritesRepo)
                ) as T
            }
        }
    )
    val state = viewModel.uiState.collectAsState().value
    val isFavorite by viewModel.isFavorite.collectAsState()
    LaunchedEffect(model) {
        viewModel.loadCar(model)
    }
    AndroidPracticeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Детали")
                        }
                    },
                    colors = TopAppBarColors(
                        Color(0xFF81A681),
                        Color(0xFF81A681),
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF)
                    ),
                    actions = {
                        if (state is UiState.Success) {
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SEND)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_SUBJECT, "Карточка")
                                        .putExtra(Intent.EXTRA_TEXT, state.data.description)
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share"
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.toggleFavorite(state.data)
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color.Red else Color.Black
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
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
                        Button(onClick = { viewModel.loadCar(model) }) {
                            Text("Retry")
                        }
                    }
                }
                is UiState.Success -> {
                    val carData = state.data
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val carAsset = context.assets
                        val img = BitmapFactory.decodeStream(carAsset.open(carData.imagePath))
                        Image(
                            bitmap = img.asImageBitmap(),
                            modifier = Modifier.height(200.dp).width(300.dp),
                            contentDescription = "Car image"
                        )
                        CarParameterText("Название автомобиля", "${carData.manufacturer} ${carData.model}")
                        CarParameterText("Годы производства", carData.productionYears)
                        CarParameterText("Класс", carData.carClass)
                        CarParameterText("Компоновка", carData.powertrainLayout)
                        CarParameterText("Описание", carData.description)
                    }
                }
            }
        }
    }
}
@Composable
fun CarParameterText(title: String, paragraph: String) {
    Column {
        Text(
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 5.dp)
                .fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(10.dp, 0.dp, 10.dp, 10.dp)
                .fillMaxWidth(),
            text = paragraph,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}