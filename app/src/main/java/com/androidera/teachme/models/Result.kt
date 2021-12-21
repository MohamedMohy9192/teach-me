package com.androidera.teachme.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "courses"
)
data class Result(
    val _class: String,
    val headline: String,
    @PrimaryKey
    val id: Int,
    val image_125_H: String,
    val image_240x135: String,
    val image_480x270: String,
    val is_paid: Boolean,
    val is_practice_test_course: Boolean,
    val price: String,
    val price_serve_tracking_id: String,
    val published_title: String,
    val title: String,
    val tracking_id: String,
    val url: String,
    val visible_instructors: List<VisibleInstructor>
) : Serializable