package uz.nurlibaydev.mytaxi.presentation.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenMapBinding
import uz.nurlibaydev.mytaxi.utils.extensions.bitmapFromVector

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 21:56
 */

class MapScreen : Fragment(R.layout.screen_map) {

    private val binding : ScreenMapBinding by viewBinding()
    private val nukus = LatLng(42.4715434, 59.4637432)
    private val tashkent = LatLng(41.2827706, 69.1392801)
    private val bukhara = LatLng(39.7776472, 64.3527575)

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

//            binding.nukus.setOnClickListener {
//                googleMap.animateCamera(cameraUpdate)
//
//            }

            val markerOptions1 = MarkerOptions()
            markerOptions1.position(nukus)
            markerOptions1.icon(bitmapFromVector(R.drawable.location_red))
            markerOptions1.anchor(0.0f, 0.5f)
            markerOptions1.title("Nukus")
            markerOptions1.snippet("This is Nukus")
            googleMap.addMarker(markerOptions1)

            val markerOptions2 = MarkerOptions()
            markerOptions2.position(tashkent)
            markerOptions2.icon(bitmapFromVector(R.drawable.location_blue))
            markerOptions2.anchor(0.0f, 0.5f)
            markerOptions2.title("Tashkent")
            markerOptions2.snippet("This is Tashkent")
            val marker2 = googleMap.addMarker(markerOptions2)
            marker2.position = tashkent

            val polylineOptions = PolylineOptions()
            polylineOptions.addAll(listOf(nukus, bukhara, tashkent))
            googleMap.addPolyline(polylineOptions)
        }
    }
}