package uz.nurlibaydev.mytaxi.presentation.main

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uz.nurlibaydev.mytaxi.R
import uz.nurlibaydev.mytaxi.databinding.ScreenMainBinding

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main), NavigationView.OnNavigationItemSelectedListener {

    private val binding: ScreenMainBinding by viewBinding()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val toolbar = mainContent.toolbar
            mainContent.toolbar.setBackgroundColor(Color.TRANSPARENT)
            navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
            val toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

            toggle.isDrawerIndicatorEnabled = false
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            navigationView.setNavigationItemSelectedListener(this@MainScreen)
            drawerLayout.setRadius(Gravity.START, 35f)
            drawerLayout.setViewScale(Gravity.START, 0.9f)
            drawerLayout.setViewElevation(Gravity.START, 20f)

            toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_drawer, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.tripHistoryScreen -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(MainScreenDirections.actionMainScreenToTripHistoryScreen())
                true
            }
            else -> false
        }
    }
}