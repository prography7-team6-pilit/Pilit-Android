package com.prography.pilit.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.prography.pilit.R
import com.prography.pilit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        mainBinding.bottomNavigation.setupWithNavController(navController)
        mainBinding.bottomNavigation.itemIconTintList = null
        navController.addOnDestinationChangedListener { _, destination, _ ->
            mainBinding.bottomNavigation.visibility = when (destination.id) {
                R.id.pillListFragment -> View.VISIBLE
                R.id.addPillFragment -> View.VISIBLE
                R.id.calendarFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    fun moveToFragment(fragmentIndex: Int) {
        mainBinding.bottomNavigation.selectedItemId = fragmentIndex
    }
}