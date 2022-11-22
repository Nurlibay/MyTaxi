package uz.nurlibaydev.mytaxi.presentation.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 16:42
 */

class SupportMapScreen : SupportMapFragment(), OnMapReadyCallback {

    private var map: (googleMap: GoogleMap) -> Unit = {}
    fun onMapReady(map: (googleMap: GoogleMap) -> Unit) {
        this.map = map
    }

    override fun onMapReady(googleMap: GoogleMap) {

    }
}