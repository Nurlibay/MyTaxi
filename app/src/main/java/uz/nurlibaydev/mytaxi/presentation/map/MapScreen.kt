package uz.nurlibaydev.mytaxi.presentation.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenMapBinding
import uz.nurlibaydev.mytaxi.utils.UiState
import uz.nurlibaydev.mytaxi.utils.isLocationEnabled
import uz.nurlibaydev.mytaxi.utils.onClick
import uz.nurlibaydev.mytaxi.utils.showMessage

@AndroidEntryPoint
class MapScreen : Fragment(R.layout.screen_map), GoogleMap.OnMarkerClickListener {

    private val binding: ScreenMapBinding by viewBinding()
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var centerScreenCoordinate: LatLng

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapScreen = childFragmentManager.findFragmentById(R.id.content_map) as SupportMapScreen
        mapScreen.getMapAsync(mapScreen)
        setupObservers()
        mapScreen.onMapReady { googleMap: GoogleMap ->
            mGoogleMap = googleMap
            mGoogleMap.uiSettings.apply {
                isCompassEnabled = false
                isRotateGesturesEnabled = false
                isMyLocationButtonEnabled = true
                isZoomControlsEnabled = true
            }
            mGoogleMap.setOnMarkerClickListener(this@MapScreen)
            mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            centerScreenCoordinate = mGoogleMap.cameraPosition.target
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(centerScreenCoordinate))
            mGoogleMap.setOnCameraMoveListener {
                centerScreenCoordinate = mGoogleMap.cameraPosition.target
            }
            mGoogleMap.setOnCameraIdleListener() {
                viewModel.getAddressByLocation(LatLng(centerScreenCoordinate.latitude, centerScreenCoordinate.longitude))
            }
        }

        binding.btnMyLocation.onClick {
            if (isLocationEnabled()) {
                if (hasPermission(ACCESS_FINE_LOCATION)) {
                    viewModel.getCurrentLocation()
                } else {
                    callPermission.launch(ACCESS_FINE_LOCATION)
                }
            } else {
                showMessage(getString(R.string.location_or_network_disable))
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.location.collect {
                when (it) {
                    is UiState.Loading -> {
                        setLoading(true)
                    }
                    is UiState.NetworkError -> {
                        showMessage(getString(R.string.network_error))
                        setLoading(false)
                    }
                    is UiState.Error -> {
                        showMessage(it.msg ?: getString(R.string.unknown_error))
                        setLoading(false)
                    }
                    is UiState.Success -> {
                        setLoading(false)
                        if (this@MapScreen::mGoogleMap.isInitialized) {
                            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(it.data, 12f)
                            mGoogleMap.animateCamera(cameraUpdate)
                        }
                    }
                    else -> {
                        setLoading(false)
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.address.collect {
                when (it) {
                    is UiState.Loading -> {
                        setLoading(true)
                    }
                    is UiState.NetworkError -> {
                        setLoading(false)
                        showMessage(getString(R.string.network_error))
                    }
                    is UiState.Error -> {
                        setLoading(false)
                        binding.tvWhereFrom.text = it.msg!!
                    }
                    is UiState.Success -> {
                        setLoading(false)
                        val address = it.data[0].getAddressLine(0)
                        binding.tvWhereFrom.text = address
                    }
                    else -> {
                        setLoading(false)
                    }
                }
            }
        }
    }

    private fun setLoading(b: Boolean) {
        binding.apply {
            progressBar.isVisible = b
            btnMyLocation.isVisible = !b
        }
    }

    private val callPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) showMessage(getString(R.string.permission_granted))
            else showMessage(getString(R.string.permission_denied))
        }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(),
            permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onMarkerClick(p0: Marker): Boolean = false
}