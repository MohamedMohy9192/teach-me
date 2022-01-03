package com.androidera.teachme.models.review

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews"
)
data class Review(
    val _class: String,
    val content: String,
    val created: String,
    @PrimaryKey
    val id: Int,
    val modified: String,
    val rating: Double,
    val user: User,
    val user_modified: String
)