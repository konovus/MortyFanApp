package com.konovus.simplemortyr

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.konovus.simplemortyr.ui.mainScreen.MainViewModel
import com.konovus.simplemortyr.util.Filter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationAndDrawer()
        initNavigationDrawer()
    }

    private fun initNavigationAndDrawer(){

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initNavigationDrawer(){
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setupWithNavController(navController = navController)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.all_characters -> viewModel.searchCharacters(Filter.NAME, "")
                R.id.alive -> viewModel.searchCharacters(Filter.STATUS_ALIVE, "alive")
                R.id.dead -> viewModel.searchCharacters(Filter.STATUS_DEAD, "dead")
                R.id.unknown -> viewModel.searchCharacters(Filter.STATUS_UNKNOWN, "unknown")
                R.id.human -> viewModel.searchCharacters(Filter.SPECIES_HUMAN, "human")
                R.id.alien -> viewModel.searchCharacters(Filter.SPECIES_ALIEN, "alien")
                R.id.humanoid -> viewModel.searchCharacters(Filter.SPECIES_HUMANOID, "humanoid")
                R.id.mythological -> viewModel.searchCharacters(Filter.SPECIES_MYTHOLOGICAL, "mythological creature")
                R.id.disease -> viewModel.searchCharacters(Filter.SPECIES_DISEASE, "disease")
                R.id.male -> viewModel.searchCharacters(Filter.GENDER_MALE, "male")
                R.id.female -> viewModel.searchCharacters(Filter.GENDER_FEMALE, "female")
                R.id.genderless -> viewModel.searchCharacters(Filter.GENDER_GENDERLESS, "genderless")
                R.id.unknown_g -> viewModel.searchCharacters(Filter.GENDER_UNKNOWN, "unknown")
            }
            drawerLayout.closeDrawer(navView)
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.fragment_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}