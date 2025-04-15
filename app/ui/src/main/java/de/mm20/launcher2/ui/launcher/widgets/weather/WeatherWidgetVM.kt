package de.mm20.launcher2.ui.launcher.widgets.weather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import de.mm20.launcher2.permissions.PermissionGroup
import de.mm20.launcher2.permissions.PermissionsManager
import de.mm20.launcher2.preferences.weather.WeatherSettings
import de.mm20.launcher2.ui.settings.SettingsActivity
import de.mm20.launcher2.weather.DailyForecast
import de.mm20.launcher2.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.max

class WeatherWidgetVM : ViewModel(), KoinComponent {
    private val weatherRepository: WeatherRepository by inject()
    private val weatherSettings: WeatherSettings by inject()

    private val permissionsManager: PermissionsManager by inject()

    val selectedDayIndex = mutableIntStateOf(0)
    val absoluteSelectedForecastIndex = mutableIntStateOf(0)

    private val forecastsFlow = weatherRepository.getDailyForecasts()

    val forecasts = mutableStateListOf<DailyForecast>()

    init {
        viewModelScope.launch {
            forecastsFlow.collectLatest {
                forecasts.clear()
                forecasts.addAll(it)
                selectNow()
            }
        }
    }

    val hasLocationPermission = permissionsManager.hasPermission(PermissionGroup.Location)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    fun requestLocationPermission(context: AppCompatActivity) {
        permissionsManager.requestPermission(context, PermissionGroup.Location)
    }
    val autoLocation = weatherSettings.autoLocation
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val imperialUnits = weatherSettings.imperialUnits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun selectNow() {
        if (forecasts.isEmpty()) return
        val now = System.currentTimeMillis()
        selectedDayIndex.intValue = max(0, forecasts.indexOfLast { it.timestamp < now })
        absoluteSelectedForecastIndex.intValue = max(0, forecasts[selectedDayIndex.intValue].hourlyForecasts.indexOfLast { it.timestamp < now })
    }

    fun openSettings(context: Context) {
        context.startActivity(
            Intent(context, SettingsActivity::class.java).apply {
                putExtra(SettingsActivity.EXTRA_ROUTE, SettingsActivity.ROUTE_WEATHER_INTEGRATION)
            }
        )
    }

    val isProviderAvailable: Flow<Boolean> = weatherRepository.getActiveProvider().map {
        it != null
    }
}