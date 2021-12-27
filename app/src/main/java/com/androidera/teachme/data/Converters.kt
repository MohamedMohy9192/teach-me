package com.androidera.teachme.data

import androidx.room.TypeConverter
import com.androidera.teachme.models.VisibleInstructor
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun instructorListToJson(value: List<VisibleInstructor>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToInstructorList(value: String) =
        Gson().fromJson(value, Array<VisibleInstructor>::class.java).toList()

}