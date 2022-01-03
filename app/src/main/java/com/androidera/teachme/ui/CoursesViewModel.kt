package com.androidera.teachme.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidera.teachme.CoursesApplication
import com.androidera.teachme.models.CoursesResponse
import com.androidera.teachme.models.Result
import com.androidera.teachme.models.review.ReviewsResponse
import com.androidera.teachme.repository.UdemyRepository
import com.androidera.teachme.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CoursesViewModel(
    application: Application,
    val coursesRepository: UdemyRepository
) : AndroidViewModel(application) {

    val TAG = CoursesViewModel::class.simpleName

    val courses: MutableLiveData<Resource<CoursesResponse>> = MutableLiveData()
    var coursesPage = 1
    var coursesResponse: CoursesResponse? = null

    val searchCourses: MutableLiveData<Resource<CoursesResponse>> = MutableLiveData()
    var searchCoursesPage = 1
    var searchCoursesResponse: CoursesResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    val courseReviews: MutableLiveData<Resource<ReviewsResponse>> = MutableLiveData()
    var courseReviewsPage = 1
    var courseReviewsResponse: ReviewsResponse? = null

    init {
        getCourses("en")
    }

    fun getCourses(language: String) = viewModelScope.launch {
        safeCoursesCall(language)
    }

    fun searchCourses(language: String, searchQuery: String) = viewModelScope.launch {
        safeSearchCoursesCall(language, searchQuery)
    }

    fun getCourseReviews(courseId: Int) = viewModelScope.launch {
        safeGetCourseReviewsCall(courseId)

    }


    private fun handleCoursesResponse(response: Response<CoursesResponse>): Resource<CoursesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                coursesPage++
                if (coursesResponse == null) {
                    coursesResponse = resultResponse
                } else {
                    val oldCourses = coursesResponse?.results
                    val newCourses = resultResponse.results
                    oldCourses?.addAll(newCourses)
                }
                return Resource.Success(coursesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchCoursesResponse(response: Response<CoursesResponse>): Resource<CoursesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (searchCoursesResponse == null || newSearchQuery != oldSearchQuery) {
                    searchCoursesPage = 1
                    oldSearchQuery = newSearchQuery
                    searchCoursesResponse = resultResponse
                } else {
                    searchCoursesPage++
                    val oldCourses = searchCoursesResponse?.results
                    val newCourses = resultResponse.results
                    oldCourses?.addAll(newCourses)
                }
                return Resource.Success(searchCoursesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCourseReviewsResponses(response: Response<ReviewsResponse>): Resource<ReviewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { reviewsResponse ->
                courseReviewsPage++
                if (courseReviewsResponse == null) {
                    courseReviewsResponse = reviewsResponse
                } else {
                    val oldReviews = courseReviewsResponse?.reviews
                    val newReviews = reviewsResponse.reviews
                    oldReviews?.addAll(newReviews)
                }
                Log.d(
                    TAG,
                    "Response In ViewModel: handleCourseReviewsResponses ${reviewsResponse.next}"
                )
                return Resource.Success(courseReviewsResponse ?: reviewsResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveCourse(course: Result) = viewModelScope.launch {
        coursesRepository.insert(course)
    }

    fun getSavedCourses() = coursesRepository.getSavedCourses()

    fun deleteCourse(course: Result) = viewModelScope.launch {
        coursesRepository.deleteCourse(course)
    }

    private suspend fun safeCoursesCall(language: String) {
        courses.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = coursesRepository.getCourses(coursesPage, language)
                Log.d(TAG, "Response In ViewModel: getCourses ${response.body()?.next}")
                courses.postValue(handleCoursesResponse(response))
            } else {
                courses.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> courses.postValue(Resource.Error("Network Failure"))
                else -> courses.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchCoursesCall(language: String, searchQuery: String) {
        newSearchQuery = searchQuery
        searchCourses.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    coursesRepository.searchCourses(searchCoursesPage, language, searchQuery)
                searchCourses.postValue(handleSearchCoursesResponse(response))
            } else {
                searchCourses.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchCourses.postValue(Resource.Error("Network Failure"))
                else -> searchCourses.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeGetCourseReviewsCall(courseId: Int) {
        courseReviews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = coursesRepository.getCourseReviews(courseId, courseReviewsPage)
                courseReviews.postValue(handleCourseReviewsResponses(response))
            } else {
                courseReviews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> courseReviews.postValue(Resource.Error("Network Failure"))
                else -> courseReviews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CoursesApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}