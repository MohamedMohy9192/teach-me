package com.androidera.teachme.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidera.teachme.models.CoursesResponse
import com.androidera.teachme.repository.CoursesRepository
import com.androidera.teachme.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CoursesViewModel(
    val coursesRepository: CoursesRepository
) : ViewModel() {

    val TAG = CoursesViewModel::class.simpleName
    val courses: MutableLiveData<Resource<CoursesResponse>> = MutableLiveData()
    val coursesPage = 1

    init {
        getCourses("en")
    }

    fun getCourses(language: String) = viewModelScope.launch {
        courses.postValue(Resource.Loading())
        val response = coursesRepository.getCourses(coursesPage, language)
        Log.d(TAG, "Response In ViewModel: getCourses ${response.body()?.next}")
        courses.postValue(handleCoursesResponse(response))
    }

    private fun handleCoursesResponse(response: Response<CoursesResponse>): Resource<CoursesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Log.d(TAG, "Response In ViewModel: handleCoursesResponse ${resultResponse.next}")
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}