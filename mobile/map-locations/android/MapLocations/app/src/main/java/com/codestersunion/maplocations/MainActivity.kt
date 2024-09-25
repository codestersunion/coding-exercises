package com.codestersunion.maplocations

import LocationRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import apiService
import com.codestersunion.maplocations.ui.theme.MapLocationsTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: LocationRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = LocationRepository(apiService)
        enableEdgeToEdge()
        setContent {
            MapLocationsTheme {
                MapScreen(
                    repository = repository
                )
            }
        }
    }
}




