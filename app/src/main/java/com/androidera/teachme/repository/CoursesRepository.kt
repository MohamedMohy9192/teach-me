package com.androidera.teachme.repository

import com.androidera.teachme.api.RetrofitInstance
import com.androidera.teachme.data.CoursesDatabase
import com.androidera.teachme.models.Result

class CoursesRepository(
    val coursesDatabase: CoursesDatabase
) {

    suspend fun getCourses(pageNumber: Int, language: String) =
        RetrofitInstance.udemyApi.getCourses(pageNumber, language)

    suspend fun searchCourses(pageNumber: Int, language: String, searchQuery: String) =
        RetrofitInstance.udemyApi.searchForCourses(pageNumber, language, searchQuery)

    suspend fun insert(course: Result) = coursesDatabase.getCoursesDao().insertCourse(course)

    fun getSavedCourses() = coursesDatabase.getCoursesDao().getAllCourses()

    suspend fun deleteCourse(course: Result) = coursesDatabase.getCoursesDao().deleteCourse(course)
}