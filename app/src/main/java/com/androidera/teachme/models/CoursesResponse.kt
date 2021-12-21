package com.androidera.teachme.models

data class CoursesResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<Result>,
    val search_tracking_id: String
)