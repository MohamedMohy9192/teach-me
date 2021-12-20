package com.androidera.teachme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidera.teachme.R
import com.androidera.teachme.models.Result
import com.bumptech.glide.Glide

class CoursesAdapter : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_course_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(course.image_125_H).into(findViewById(R.id.course_image_view))
            findViewById<TextView>(R.id.course_title_text_view).text = course.title
            findViewById<TextView>(R.id.course_prince_text_view).text = course.price
            findViewById<TextView>(R.id.course_instructor_text_view).text =
                course.visible_instructors[0].title
            onCourseItemClickListener?.let {
                it(course)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onCourseItemClickListener: ((Result) -> Unit)? = null

    fun setOnCourseItemClickListener(listener: (Result) -> Unit) {
        this.onCourseItemClickListener = listener
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}