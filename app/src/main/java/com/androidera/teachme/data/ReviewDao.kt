package com.androidera.teachme.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidera.teachme.models.review.Result

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseReviews(reviews: List<Result>)

    @Query("SELECT * FROM reviews")
     fun getCourseReviews(): LiveData<List<Result>>
}