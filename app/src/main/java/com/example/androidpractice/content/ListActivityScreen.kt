package com.example.androidpractice.content

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.androidpractice.DetailsActivity
import com.example.androidpractice.di.AppModule
import com.example.androidpractice.presentation.ui.UiState
import com.example.androidpractice.presentation.viewmodel.CarListViewModel
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListActivityScreen(navController: NavHostController) {
    val viewModel = viewModel<CarListViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return CarListViewModel(AppModule.getCarsUseCase) as T
            }
        }
    )
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val dataStore = AppModule.provideDataStore(context)
    val filterCache = AppModule.filterCache

    val dataStoreState by dataStore.data.collectAsState(initial = emptyMap<Preferences.Key<*>, Any?>())
    LaunchedEffect(dataStoreState) {
        val prefs = dataStore.data.first()
        filterCache.setFiltersApplied(
            !prefs[PreferencesKeys.MANUFACTURER].isNullOrBlank() ||
                    prefs[PreferencesKeys.MIN_YEAR] != 0 ||
                    !prefs[PreferencesKeys.CAR_CLASS].isNullOrBlank()
        )
        viewModel.loadCarsWithFilters(dataStore)
    }

    AndroidPracticeTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    IconButton(onClick = { navController.navigate(NavigationRoutes.Filters.route) }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Filter",
                            tint = Color.Black
                        )
                    }
                    if (filterCache.hasFiltersApplied) {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
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
                            Button(onClick = { viewModel.loadCarsWithFilters(dataStore) }) {
                                Text("Retry")
                            }
                        }
                    }
                    is UiState.Success -> {
                        val itemsArray = state.data
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            items(itemsArray) { item ->
                                val intent = Intent(context, DetailsActivity::class.java)
                                intent.putExtra("CAR_MODEL", item.model)
                                Button(
                                    onClick = {
                                        context.startActivity(intent)
                                    },
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
    }
}







