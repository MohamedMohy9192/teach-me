package com.androidera.teachme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidera.teachme.repository.CoursesRepository

class CoursesViewModelProviderFactory(
    private val  coursesRepository: CoursesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoursesViewModel(coursesRepository) as T
    }
}