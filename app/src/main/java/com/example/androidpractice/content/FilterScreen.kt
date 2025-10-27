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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.androidpractice.di.AppModule
import com.example.androidpractice.presentation.viewmodel.CarListViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavHostController) {
    val viewModel = viewModel<CarListViewModel>(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return CarListViewModel(AppModule.getCarsUseCase) as T
            }
        }
    )
    val context = androidx.compose.ui.platform.LocalContext.current
    val dataStore: DataStore<Preferences> = AppModule.provideDataStore(context)

    var manufacturer by remember { mutableStateOf("") }
    var minYear by remember { mutableStateOf("") }
    var carClass by remember { mutableStateOf("") }

    // Load saved filters
    LaunchedEffect(Unit) {
        runBlocking {
            val prefs = dataStore.data.first()
            manufacturer = prefs[PreferencesKeys.MANUFACTURER] ?: ""
            minYear = (prefs[PreferencesKeys.MIN_YEAR]?.toString() ?: "")
            carClass = prefs[PreferencesKeys.CAR_CLASS] ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = manufacturer,
            onValueChange = { manufacturer = it },
            label = { Text("Manufacturer") },
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = minYear,
            onValueChange = { minYear = it },
            label = { Text("Min Year") },
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = carClass,
            onValueChange = { carClass = it },
            label = { Text("Car Class") },
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = {
                runBlocking {
                    dataStore.edit { preferences ->
                        preferences[PreferencesKeys.MANUFACTURER] = manufacturer
                        preferences[PreferencesKeys.MIN_YEAR] = minYear.toIntOrNull() ?: 0
                        preferences[PreferencesKeys.CAR_CLASS] = carClass
                    }
                }
                viewModel.loadCarsWithFilters(dataStore)
                navController.popBackStack()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Done")
        }
    }
}