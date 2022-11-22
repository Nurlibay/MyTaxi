package uz.nurlibaydev.mytaxi.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenProfileBinding

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 16:09
 */

class ProfileScreen: Fragment(R.layout.screen_profile) {

    private val binding: ScreenProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}