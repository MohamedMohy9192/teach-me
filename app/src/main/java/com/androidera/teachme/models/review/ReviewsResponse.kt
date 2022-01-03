package com.androidera.teachme.models.review

import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    @SerializedName("results")
    val reviews: MutableList<Review>
)