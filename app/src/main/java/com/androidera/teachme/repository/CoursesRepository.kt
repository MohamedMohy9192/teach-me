package com.androidera.teachme.repository

import com.androidera.teachme.api.RetrofitInstance
import com.androidera.teachme.data.CoursesDatabase

class CoursesRepository(
    val coursesDatabase: CoursesDatabase
) {

    suspend fun getCourses(pageNumber: Int, language: String) =
        RetrofitInstance.udemyApi.getCourses(pageNumber, language)
}