package com.androidera.teachme.api

import com.androidera.teachme.models.CoursesResponse
import com.androidera.teachme.models.review.ReviewResponse
import com.androidera.teachme.util.Constants.Companion.AUTHORIZATION
import com.androidera.teachme.util.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UdemyService {

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

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization: $AUTHORIZATION",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses/{course_id}/reviews")
    suspend fun getCourseReviews(
        @Path("course_id") courseId: Int,
        @Query("page") pageNumber: Int = 1,
        @Query("page_size") pageSize: Int = QUERY_PAGE_SIZE
    ): Response<ReviewResponse>
}