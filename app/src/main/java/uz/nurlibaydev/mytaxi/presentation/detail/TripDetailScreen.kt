package uz.nurlibaydev.mytaxi.presentation.detail

import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import uz.nurlibaydev.mytaxi.databinding.ScreenTripDetailBinding
import uz.nurlibaydev.mytaxi.presentation.map.SupportMapScreen
import uz.nurlibaydev.mytaxi.utils.UiState
import uz.nurlibaydev.mytaxi.utils.onClick
import uz.nurlibaydev.mytaxi.utils.showMessage
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.utils.bitmapFromVector

@AndroidEntryPoint
class TripDetailScreen: Fragment(R.layout.screen_trip_detail) {

    private val binding: ScreenTripDetailBinding by viewBinding()
    private val viewModel: TripDetailViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private val args: TripDetailScreenArgs by navArgs()
    private var line: Polyline? = null
    private var locationManager: LocationManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        binding.apply {
            iconBack.onClick {
                findNavController().popBackStack()
            }
            tvWhereFrom.text = args.destinationData.fromWhere
            tvWhereTo.text = args.destinationData.toWhere
            val mapContainer =
                childFragmentManager.findFragmentById(R.id.trip_detail_map_container) as SupportMapScreen
            mapContainer.getMapAsync(mapContainer)
            mapContainer.onMapReady {
                googleMap = it
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                googleMap.uiSettings.apply {
                    isCompassEnabled = false
                }
                val whereFrom = args.destinationData.start
                val whereTo = args.destinationData.end
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(whereFrom)
                    .title(args.destinationData.fromWhere)
                    .icon(bitmapFromVector(R.drawable.ic_target_red)))
                googleMap.addMarker(MarkerOptions().position(whereTo)
                    .title(args.destinationData.toWhere)
                    .icon(bitmapFromVector(R.drawable.ic_target_blue)))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(whereFrom, 18F))
            }
            bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView)
            val bottomSheetHeight = (resources.displayMetrics.heightPixels * 0.6).toInt()
            bottomSheetBehavior.peekHeight = bottomSheetHeight

            viewModel.getRoutingPath(args.destinationData.start, args.destinationData.end)
        }
    }

    override fun onPause() {
        locationManager = null
        super.onPause()
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.route.collect {
                when (it) {
                    is UiState.Loading -> {
                        showMessage("Loading")
                    }
                    is UiState.NetworkError -> {
                        showMessage("NetworkError")
                    }
                    is UiState.Error -> {
                        showMessage(it.msg!!)
                    }
                    is UiState.Success -> {
                        val listPoints: List<LatLng> = it.data.routeList?.get(0)!!.points
                        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
                        val iterator = listPoints.iterator()
                        while (iterator.hasNext()) {
                            val data = iterator.next()
                            options.add(data)
                        }
                        line?.remove()
                        line = googleMap.addPolyline(options)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(it.data.routeList!![0].latLgnBounds.center))
                        val builder = LatLngBounds.Builder()
                        builder.include(args.destinationData.start)
                        builder.include(args.destinationData.end)
                        val bounds = builder.build()
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
                    }
                    else -> {
                        showMessage("Unknown Error!")
                    }
                }
            }
        }
    }
}