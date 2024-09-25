package com.codestersunion.maplocations

import LocationData
import LocationRepository
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MapScreen(repository: LocationRepository) {
    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    var locationTypes by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }
    val filteredLocations by remember(locations, selectedType) {
        mutableStateOf(locations.filter { it.locationType == selectedType || selectedType == null })
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            locations = repository.fetchLocations()
            locationTypes = locations.map { it.locationType }.distinct()
        } catch (e: Exception) {
            scope.launch {
                Toast.makeText(context, "Error fetching locations: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Map(
            locations = filteredLocations,
            selectedLocation = selectedLocation,
            onLocationSelect = { location ->
                selectedLocation = location
            }
        )

        // Overlays
        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(locationTypes) { type ->
                    Button(
                        onClick = { selectedType = if (selectedType == type) null else type },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedType == type) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = type.replaceFirstChar {
                                it.uppercase()
                            },
                            color = if (selectedType == type) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}