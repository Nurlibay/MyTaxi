package uz.nurlibaydev.mytaxi.presentation.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenMapBinding
import uz.nurlibaydev.mytaxi.utils.extensions.onClick

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 21:56
 */

class MapScreen : Fragment(R.layout.screen_map) {

    private val binding: ScreenMapBinding by viewBinding()
    private val nukus = LatLng(42.4715434, 59.4637432)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.content_map) as SupportMapScreen
        mapFragment.getMapAsync(mapFragment)
        mapFragment.onMapReady { googleMap: GoogleMap ->
            googleMap.uiSettings.apply {
                isCompassEnabled = false
                isRotateGesturesEnabled = false
                isMyLocationButtonEnabled = true
            }
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap.setOnCameraMoveListener {
                val center = googleMap.cameraPosition.target
            }
            googleMap.setOnCameraIdleListener {
                val center = googleMap.cameraPosition.target
            }
            googleMap.setOnCameraMoveStartedListener {
                val center = googleMap.cameraPosition.target
            }
            val cameraPosition = CameraPosition.fromLatLngZoom(nukus, 10f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

            binding.btnMyLocation.onClick {
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }
}