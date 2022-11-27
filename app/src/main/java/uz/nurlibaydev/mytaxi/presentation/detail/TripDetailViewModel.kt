package uz.nurlibaydev.mytaxi.presentation.detail

import androidx.lifecycle.ViewModel
import com.directions.route.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.app.App
import uz.nurlibaydev.mytaxi.data.models.RouteData
import uz.nurlibaydev.mytaxi.utils.UiState
import javax.inject.Inject

@HiltViewModel
class TripDetailViewModel @Inject constructor() : ViewModel(), RoutingListener {

    private val _route = MutableStateFlow<UiState<RouteData>>(UiState.Empty)
    val route: StateFlow<UiState<RouteData>> = _route

    fun getRoutingPath(from: LatLng, to: LatLng) {
        try {
            val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(from, to)
                .key(App.instance.getString(R.string.google_maps_key))
                .build()
            routing.execute()
        } catch (e: Exception) {
            _route.value = UiState.Error(e.localizedMessage)
        }
    }

    override fun onRoutingFailure(e: RouteException?) {
        _route.value = UiState.Error(e?.localizedMessage)
    }

    override fun onRoutingStart() {
        _route.value = UiState.Loading
    }

    override fun onRoutingSuccess(list: ArrayList<Route>?, p1: Int) {
        _route.value = UiState.Success(RouteData(list, p1))
    }

    override fun onRoutingCancelled() {
        _route.value = UiState.Error("Cancelled")
    }
}