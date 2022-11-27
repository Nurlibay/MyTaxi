package uz.nurlibaydev.mytaxi.domain.repository

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import uz.nurlibaydev.mytaxi.utils.UiState
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val geocoder: Geocoder,
    private val fusedLocationClient: FusedLocationProviderClient,
) : MainRepository {

    override fun getAddressByLocation(latLng: LatLng): Flow<UiState<List<Address>>> = flow {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        emit(UiState.Loading)
        if (addresses.isNotEmpty()) {
            emit(UiState.Success(addresses))
        } else {
            emit(UiState.Error("Addresses are empty!"))
        }
    }.catch {
        emit(UiState.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<UiState<LatLng>> = callbackFlow  {
        trySend(UiState.Loading)
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                trySend(UiState.Success(LatLng(task.result.latitude, task.result.longitude)))
            } else {
                trySend(UiState.Error(task.exception?.localizedMessage?: "Unknown Error"))
            }
        }.addOnFailureListener {
            trySend(UiState.Error(it.message.toString()))
        }
        awaitClose {  }
    }.catch {
        emit(UiState.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}