package uz.nurlibaydev.mytaxi.presentation.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.data.source.LocalData
import uz.nurlibaydev.mytaxi.databinding.ScreenTripHistoryBinding

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 16:09
 */

class TripHistoryScreen: Fragment(R.layout.screen_trip_history) {

    private val binding: ScreenTripHistoryBinding by viewBinding()
    private val adapter by lazy { TripHistoryAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            rvTripHistory.adapter = adapter
            adapter.submitList(LocalData.tripHistoryList)
        }
    }
}