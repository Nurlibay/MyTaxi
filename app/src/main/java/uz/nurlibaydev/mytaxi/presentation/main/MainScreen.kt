package uz.nurlibaydev.mytaxi.presentation.main

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
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

    private var drawer: AdvanceDrawerLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawer = binding.drawerLayout
        val toolbar = requireActivity().findViewById<View>(R.id.toolbar) as Toolbar
        val toggle = ActionBarDrawerToggle(requireActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        drawer!!.useCustomBehavior(Gravity.START)
        drawer!!.useCustomBehavior(Gravity.END)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }
}