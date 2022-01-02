package com.androidera.teachme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidera.teachme.models.Result

@Database(
    entities = [Result::class, com.androidera.teachme.models.review.Result::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CoursesDatabase : RoomDatabase() {

    abstract fun getCoursesDao(): CoursesDao
    abstract fun getReviewsDao(): ReviewDao

    companion object {
        @Volatile
        private var instance: CoursesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CoursesDatabase::class.java,
                "courses_database"
            ).build()
    }
}