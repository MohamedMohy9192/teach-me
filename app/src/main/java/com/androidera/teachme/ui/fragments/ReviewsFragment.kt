package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidera.teachme.adapters.ReviewsAdapter
import com.androidera.teachme.databinding.FragmentReviewsBinding
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Resource

class ReviewsFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var courseReviewsAdapter: ReviewsAdapter
    val args: ReviewsFragmentArgs by navArgs()
    private val TAG = ReviewsFragment::class.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as CoursesActivity).viewModel
        setupRecyclerView()

        val courseId = args.courseId

        viewModel.getCourseReviews(courseId)

        viewModel.courseReviews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { reviewsResponse ->

                        courseReviewsAdapter.differ.submitList(reviewsResponse.reviews)
                        Log.i(TAG, "Reviews response: ${reviewsResponse.reviews}")

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun setupRecyclerView() {
        courseReviewsAdapter = ReviewsAdapter()
        binding.reviewsRecyclerView.apply {
            adapter = courseReviewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}