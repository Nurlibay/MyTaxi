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

@AndroidEntryPoint
class TripDetailScreen: Fragment(R.layout.screen_trip_detail) {

    private val binding: ScreenTripDetailBinding by viewBinding()
    private val viewModel: TripDetailViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private val args: TripDetailScreenArgs by navArgs()
    private var line: Polyline? = null
    private var locationManager: LocationManager? = null
    private var polylines: ArrayList<Polyline>? = null

    //Global flags
    private var firstRefresh = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        binding.apply {
            iconBack.onClick {
                findNavController().popBackStack()
            }
            tvWhereFrom.text = args.destinationData.fromWhere
            tvWhereTo.text = args.destinationData.toWhere
            val mapContainer = childFragmentManager.findFragmentById(R.id.trip_detail_map_container) as SupportMapScreen
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
                googleMap.addMarker(MarkerOptions().position(whereFrom).title(args.destinationData.fromWhere))
                googleMap.addMarker(MarkerOptions().position(whereTo).title(args.destinationData.toWhere))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(whereFrom, 18F))
            }
            bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView)
            val peekHeight = (resources.displayMetrics.heightPixels * 0.6).toInt()
            bottomSheetBehavior.peekHeight = peekHeight

            viewModel.getRoutingPath(args.destinationData.start, args.destinationData.end)
        }
    }

    override fun onResume() {
        super.onResume()
        firstRefresh = true
//        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
//        if (!PermissionChecker.getInstance().checkGPSPermission(this, locationManager)) {
//            //GPS not enabled for the application.
//        } else if (!PermissionCheck.getInstance().checkLocationPermission(this)) {
//            //Location permission not given.
//        } else {
//            showMessage("Fetching Location")
//            try {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this@TripDetailScreen)
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this)
//            } catch (e: java.lang.Exception) {
//                showMessage("ERROR: Cannot start location listener")
//            }
//        }
    }

    override fun onPause() {
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//
//            }
//            try {
//                locationManager.removeUpdates(this@TripDetailScreen)
//            } catch (e: Exception) {
//                e.localizedMessage?.let { showMessage(it) }
//            }
//        }
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