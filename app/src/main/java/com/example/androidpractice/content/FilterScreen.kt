package com.example.androidpractice.content
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.androidpractice.di.AppModule
import com.example.androidpractice.presentation.viewmodel.FilterViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = viewModel<FilterViewModel>(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return FilterViewModel(
                    AppModule.provideDataStore(context),
                    AppModule.filterCache
                ) as T
            }
        }
    )
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.manufacturer,
            onValueChange = { viewModel.updateManufacturer(it) },
            label = { Text("Manufacturer") },
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = state.minYear,
            onValueChange = { viewModel.updateMinYear(it) },
            label = { Text("Min Year") },
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = state.carClass,
            onValueChange = { viewModel.updateCarClass(it) },
            label = { Text("Car Class") },
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = {
                viewModel.applyFilters()
                navController.popBackStack()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Done")
        }
    }
}