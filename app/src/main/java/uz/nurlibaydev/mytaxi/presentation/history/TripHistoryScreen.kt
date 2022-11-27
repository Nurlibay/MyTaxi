package uz.nurlibaydev.mytaxi.presentation.history

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.data.source.LocalData
import uz.nurlibaydev.mytaxi.databinding.ScreenTripHistoryBinding

@AndroidEntryPoint
class TripHistoryScreen : Fragment(R.layout.screen_trip_history) {

    private val binding: ScreenTripHistoryBinding by viewBinding()
    private val adapter by lazy { TripHistoryAdapter() }
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private var isCreated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            rvTripHistory.adapter = adapter
            if(!isCreated){
                shimmerLayout.startShimmer()
                Handler(Looper.getMainLooper()).postDelayed({
                    shimmerLayout.visibility = View.INVISIBLE
                    rvTripHistory.visibility = View.VISIBLE
                }, 4000)
                isCreated = true
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.INVISIBLE
                rvTripHistory.visibility = View.VISIBLE
            }
            adapter.submitList(LocalData.tripHistoryList)
            adapter.setOnItemClickListener {
                navController.navigate(TripHistoryScreenDirections.actionTripHistoryScreenToTripDetailScreen(it.destinationData))
            }
        }
    }
}