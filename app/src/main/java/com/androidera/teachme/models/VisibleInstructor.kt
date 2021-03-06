package com.androidera.teachme.models

import java.io.Serializable

data class VisibleInstructor(
    val _class: String,
    val display_name: String,
    val image_100x100: String,
    val image_50x50: String,
    val initials: String,
    val job_title: String,
    val name: String,
    val title: String,
    val url: String
) : Serializable