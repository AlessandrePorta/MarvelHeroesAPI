package com.marvelapi

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.marvelheroesapi.R
import com.marvelheroesapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fcCharacters) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setupNavController(binding.toolbarApp)
        setSupportActionBar(binding.toolbarApp)
    }

    private fun setupNavController(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = ""
            val isTopLevelDestinations =
                appBarConfiguration.topLevelDestinations.contains(destination.id)
            if (!isTopLevelDestinations) {
                getToolbarGone()
            }
        }
    }

    fun getToolbarGone() {
        supportActionBar?.hide()
    }

    fun getToolbarShow() {
        supportActionBar?.show()
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }
}
