package uz.nurlibaydev.mytaxi.presentation.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenMapBinding
import uz.nurlibaydev.mytaxi.utils.isLocationEnabled
import uz.nurlibaydev.mytaxi.utils.onClick
import uz.nurlibaydev.mytaxi.utils.showMessage
import java.util.*


class MapScreen : Fragment(R.layout.screen_map), GoogleMap.OnMarkerClickListener {

    private val binding: ScreenMapBinding by viewBinding()
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var centerScreenCoordinate: LatLng
    private lateinit var geocoder : Geocoder
    private lateinit var addresses: List<Address>

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapScreen = childFragmentManager.findFragmentById(R.id.content_map) as SupportMapScreen
        mapScreen.getMapAsync(mapScreen)
        geocoder = Geocoder(requireContext(), Locale.getDefault())
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
                mGoogleMap.setOnCameraIdleListener() {
                    getAddressByLocation(centerScreenCoordinate.latitude, centerScreenCoordinate.longitude)
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnMyLocation.onClick {
            if(isLocationEnabled()){
                if(hasPermission(ACCESS_FINE_LOCATION)){
                    findMyLocation()
                } else {
                    callPermission.launch(ACCESS_FINE_LOCATION)
                }
            } else {
                showMessage("Location or Network disable. Please enable all")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAddressByLocation(latitude: Double, longitude: Double) {
        addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if(this::addresses.isInitialized) {
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            binding.tvWhereFrom.text = "$address $city"
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun findMyLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    if(this::mGoogleMap.isInitialized) {
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(lastLocation.latitude, lastLocation.longitude)))
                    }
                } else {
                    showMessage("GetLastLocation:exception ${task.exception}")
                    showMessage(getString(R.string.no_location_detected))
                }
            }
    }

    private val callPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if(result) showMessage(getString(R.string.permission_granted))
        else showMessage(getString(R.string.permission_denied))
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onMarkerClick(p0: Marker): Boolean = false
}