package com.androidera.teachme.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidera.teachme.repository.UdemyRepository

class CoursesViewModelProviderFactory(
    private val application: Application,
    private val coursesRepository: UdemyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoursesViewModel(application, coursesRepository) as T
    }
}