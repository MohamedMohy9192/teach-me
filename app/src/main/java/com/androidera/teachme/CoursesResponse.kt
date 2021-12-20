package com.androidera.teachme

data class CoursesResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>,
    val search_tracking_id: String
)