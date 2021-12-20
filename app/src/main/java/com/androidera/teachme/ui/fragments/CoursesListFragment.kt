package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidera.teachme.adapters.CoursesAdapter
import com.androidera.teachme.databinding.FragmentCoursesListBinding
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Resource

class CoursesListFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel
    private var fragmentCoursesListBinding: FragmentCoursesListBinding? = null
    private lateinit var coursesAdapter: CoursesAdapter

    private val TAG = "CoursesListFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCoursesListBinding.bind(view)
        fragmentCoursesListBinding = binding

        viewModel = (activity as CoursesActivity).viewModel


        setupRecyclerView()

        viewModel.courses.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { coursesResponse ->
                        coursesAdapter.differ.submitList(coursesResponse.results)


                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        fragmentCoursesListBinding?.paginationProgressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        fragmentCoursesListBinding?.paginationProgressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter()
        fragmentCoursesListBinding?.coursesRecyclerView?.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        fragmentCoursesListBinding = null
        super.onDestroyView()
    }
}