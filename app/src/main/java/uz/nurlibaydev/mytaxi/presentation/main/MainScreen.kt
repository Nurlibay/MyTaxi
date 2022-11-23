package uz.nurlibaydev.mytaxi.presentation.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenMainBinding
import uz.nurlibaydev.mytaxi.utils.AdvanceDrawerLayout

/**
 *  Created by Nurlibay Koshkinbaev on 22/11/2022 16:07
 */

class MainScreen : Fragment(R.layout.screen_main), NavigationView.OnNavigationItemSelectedListener {

    private val binding: ScreenMainBinding by viewBinding()
    private lateinit var navController: NavController

    private lateinit var drawer: AdvanceDrawerLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawer = binding.drawerLayout
        val toolbar = binding.appBar.toolbar
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        val toggle = ActionBarDrawerToggle(requireActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_drawer, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.tripHistoryScreen -> {
                drawer.closeDrawer(GravityCompat.START)
                navController.navigate(MainScreenDirections.actionMainScreenToTripHistoryScreen())
                true
            }
            else -> false
        }
    }
}