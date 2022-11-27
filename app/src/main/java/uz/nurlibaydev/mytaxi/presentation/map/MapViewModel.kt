package uz.nurlibaydev.mytaxi.presentation.map

import android.location.Address
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.nurlibaydev.mytaxi.domain.usecases.MainUseCase
import uz.nurlibaydev.mytaxi.utils.UiState
import uz.nurlibaydev.mytaxi.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
): ViewModel() {

    private val _location = MutableStateFlow<UiState<LatLng>>(UiState.Empty)
    val location: StateFlow<UiState<LatLng>> = _location

    private val _address = MutableStateFlow<UiState<List<Address>>>(UiState.Empty)
    val address: StateFlow<UiState<List<Address>>> = _address

    fun getCurrentLocation() {
        if (!hasConnection()) {
            _location.value = UiState.NetworkError("No Internet Connection!")
            return
        }
        viewModelScope.launch {
            _location.value = UiState.Loading
            mainUseCase.getCurrentLocation().collect {
                when (it) {
                    is UiState.Success -> {
                        _location.value = UiState.Success(it.data)
                    }
                    is UiState.Error -> {
                        _location.value = UiState.Error(it.msg)
                    }
                    else -> {}
                }
            }
        }
    }

    fun getAddressByLocation(latLng: LatLng) {
        if (!hasConnection()) {
            _address.value = UiState.NetworkError("No Internet Connection!")
            return
        }
        viewModelScope.launch {
            _address.value = UiState.Loading
            mainUseCase.getAddressByLocation(latLng).collect {
                when (it) {
                    is UiState.Success -> {
                        _address.value = UiState.Success(it.data)
                    }
                    is UiState.Error -> {
                        _address.value = UiState.Error(it.msg)
                    }
                    else -> {}
                }
            }
        }
    }
}