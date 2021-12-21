package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidera.teachme.R
import com.androidera.teachme.adapters.CoursesAdapter
import com.androidera.teachme.databinding.FragmentCoursesListBinding
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Constants.Companion.COURSE_BUNDLE_KEY
import com.androidera.teachme.util.Resource

class CoursesListFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel
    private var _binding: FragmentCoursesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var coursesAdapter: CoursesAdapter

    private val TAG = CoursesListFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as CoursesActivity).viewModel
        setupRecyclerView()

        coursesAdapter.setOnCourseItemClickListener { course ->
            val bundle = Bundle().apply {
                putSerializable(COURSE_BUNDLE_KEY, course)
            }
            findNavController().navigate(
                R.id.action_coursesListFragment_to_coursesDetailFragment,
                bundle
            )
        }

        viewModel.courses.observe(viewLifecycleOwner, { response ->
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
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter()
        binding.coursesRecyclerView.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}