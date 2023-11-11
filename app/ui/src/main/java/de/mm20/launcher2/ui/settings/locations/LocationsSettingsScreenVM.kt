package de.mm20.launcher2.ui.settings.locations

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.mm20.launcher2.preferences.LauncherDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LocationsSettingsScreenVM: ViewModel(), KoinComponent {
    private val dataStore: LauncherDataStore by inject()

    val locations = dataStore.data.map { it.locationsSearch.enabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    fun setLocations(openStreetMaps: Boolean) {
        viewModelScope.launch {
            dataStore.updateData {
                it.toBuilder()
                    .setLocationsSearch(
                        it.locationsSearch.toBuilder()
                            .setEnabled(openStreetMaps)
                    )
                    .build()
            }
        }
    }

    val insaneUnits = dataStore.data.map { it.locationsSearch.useInsaneUnits }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    fun setInsaneUnits(insaneUnits: Boolean) {
        viewModelScope.launch {
            dataStore.updateData {
                it.toBuilder()
                    .setLocationsSearch(
                        it.locationsSearch.toBuilder()
                            .setUseInsaneUnits(insaneUnits)
                    )
                    .build()
            }
        }
    }

    val radius = dataStore.data.map { it.locationsSearch.searchRadius }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1500)
    fun setRadius(radius: Int) {
        viewModelScope.launch {
            dataStore.updateData {
                it.toBuilder()
                    .setLocationsSearch(
                        it.locationsSearch.toBuilder()
                            .setSearchRadius(radius)
                    )
                    .build()
            }
        }
    }

    val customUrl = dataStore.data.map { it.locationsSearch.customUrl }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    fun setCustomUrl(customUrl: String) {
        var customUrl = customUrl
        if (customUrl.endsWith('/'))
            customUrl = customUrl.substring(0, customUrl.length - 1)
        if (customUrl.endsWith("/api/interpreter"))
            customUrl = customUrl.substring(0, customUrl.length - "/api/interpreter".length)

        viewModelScope.launch {
            dataStore.updateData {
                it.toBuilder()
                    .setLocationsSearch(
                        it.locationsSearch.toBuilder()
                            .setCustomUrl(customUrl)
                    )
                    .build()
            }
        }
    }
}