package uz.nurlibaydev.mytaxi.domain.repository

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import uz.nurlibaydev.mytaxi.utils.UiState

interface MainRepository {
    fun getCurrentLocation(): Flow<UiState<LatLng>>
    fun getAddressByLocation(latLng: LatLng): Flow<UiState<List<Address>>>
}