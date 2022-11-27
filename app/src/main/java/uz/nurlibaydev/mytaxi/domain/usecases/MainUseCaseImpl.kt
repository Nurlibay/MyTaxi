package uz.nurlibaydev.mytaxi.domain.usecases

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import uz.nurlibaydev.mytaxi.domain.repository.MainRepository
import uz.nurlibaydev.mytaxi.utils.UiState
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(
    private val mainRepository: MainRepository
) : MainUseCase {
    override fun getCurrentLocation(): Flow<UiState<LatLng>> {
        return mainRepository.getCurrentLocation()
    }

    override fun getAddressByLocation(latLng: LatLng): Flow<UiState<List<Address>>> {
        return mainRepository.getAddressByLocation(latLng)
    }
}