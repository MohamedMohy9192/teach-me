package com.androidera.teachme.ui

import androidx.lifecycle.ViewModel
import com.androidera.teachme.repository.CoursesRepository

class CoursesViewModel(
    val coursesRepository: CoursesRepository
) : ViewModel() {
}