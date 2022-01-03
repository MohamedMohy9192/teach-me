package com.androidera.teachme.models.review

data class ReviewsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val reviews: MutableList<Review>
)