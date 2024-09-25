package com.codestersunion.maplocations

import LocationData
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun Map(
    locations: List<LocationData>,
    selectedLocation: LocationData?,
    onLocationSelect: (LocationData) -> Unit = {}
) {
    val sanFrancisco = LatLng(37.7749, -122.4194)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sanFrancisco, 10f)
    }
    val mapUiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }

    LaunchedEffect(locations) {
        if (locations.isNotEmpty()) {
            val bounds = LatLngBounds.builder().apply {
                locations.forEach { location ->
                    include(LatLng(location.latitude, location.longitude))
                }
            }.build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings
    ) {
        locations.forEach { location ->
            var localSelectedLocation by remember { mutableStateOf(selectedLocation) }

            MarkerInfoWindow(state = MarkerState(
                position = LatLng(
                    location.latitude,
                    location.longitude
                )
            ),
                title = "Custom Marker",
                icon = BitmapDescriptorFactory.defaultMarker(if (location == selectedLocation) BitmapDescriptorFactory.HUE_BLUE else BitmapDescriptorFactory.HUE_RED),
                onClick = {
                    onLocationSelect(location)
                    localSelectedLocation = location
                    false
                }) {
                CustomMarkerInfoWindow(location = location)
            }
        }
    }
}