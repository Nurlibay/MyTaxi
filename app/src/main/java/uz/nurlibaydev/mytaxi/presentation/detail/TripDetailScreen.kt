package uz.nurlibaydev.mytaxi.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenTripDetailBinding
import uz.nurlibaydev.mytaxi.presentation.map.SupportMapScreen
import uz.nurlibaydev.mytaxi.utils.extensions.onClick

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 16:11
 */

class TripDetailScreen: Fragment(R.layout.screen_trip_detail) {

    private val binding: ScreenTripDetailBinding by viewBinding()
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            iconBack.onClick {
                findNavController().popBackStack()
            }
            val mapContainer = childFragmentManager.findFragmentById(R.id.trip_detail_map_container) as SupportMapScreen
            mapContainer.getMapAsync(mapContainer)
            mapContainer.onMapReady {
                googleMap = it
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                googleMap.uiSettings.apply {
                    isCompassEnabled = false
                }
            }
            bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView)
            val peekHeight = (resources.displayMetrics.heightPixels * 0.6).toInt()
            bottomSheetBehavior.peekHeight = peekHeight
        }
    }
}