package com.androidera.teachme.api

import com.androidera.teachme.CoursesResponse
import com.androidera.teachme.util.Constants.Companion.AUTHORIZATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoursesService {

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization: $AUTHORIZATION",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses")
    suspend fun getCourses(
        @Query("page")
        pageNumber: Int = 1,
        @Query("language")
        language: String = "en"
    ): Response<CoursesResponse>

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization: $AUTHORIZATION",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses")
    suspend fun searchForCourses(
        @Query("page")
        pageNumber: Int = 1,
        @Query("language")
        language: String = "en",
        @Query("search")
        searchInput: String
    ): Response<CoursesResponse>
}