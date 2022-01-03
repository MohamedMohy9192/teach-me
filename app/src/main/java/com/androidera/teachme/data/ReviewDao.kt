package com.androidera.teachme.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidera.teachme.models.review.Review

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseReviews(reviews: List<Review>)

    @Query("SELECT * FROM reviews")
     fun getCourseReviews(): LiveData<List<Review>>
}