package com.androidera.teachme.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidera.teachme.R
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel

class CoursesListFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as CoursesActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses_list, container, false)
    }


}