package com.androidera.teachme.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androidera.teachme.models.Result

@Dao
interface CoursesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Result): Long

    @Query("SELECT * FROM courses")
    fun getAllCourses(): LiveData<List<Result>>

    @Delete
    suspend fun deleteCourse(course: Result)

}