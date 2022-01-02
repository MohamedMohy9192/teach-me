package com.androidera.teachme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidera.teachme.R
import com.androidera.teachme.data.CoursesDatabase
import com.androidera.teachme.databinding.ActivityCoursesBinding
import com.androidera.teachme.repository.CoursesRepository

class CoursesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoursesBinding
    lateinit var viewModel: CoursesViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val coursesRepository = CoursesRepository(CoursesDatabase(this))
        val viewModelProviderFactory =
            CoursesViewModelProviderFactory(application, coursesRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[CoursesViewModel::class.java]


        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.coursesNavHostFragment
        ) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.coursesNavHostFragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }
}