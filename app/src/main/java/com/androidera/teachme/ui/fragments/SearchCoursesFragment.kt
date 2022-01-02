package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidera.teachme.R
import com.androidera.teachme.adapters.CoursesAdapter
import com.androidera.teachme.databinding.FragmentSearchCoursesBinding
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Constants
import com.androidera.teachme.util.Constants.Companion.SEARCH_COURSES_TIME_DELAY
import com.androidera.teachme.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchCoursesFragment : Fragment() {

    private var _binding: FragmentSearchCoursesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CoursesViewModel
    private lateinit var coursesAdapter: CoursesAdapter
    val TAG = SearchCoursesFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as CoursesActivity).viewModel
        setupRecyclerView()

        coursesAdapter.setOnCourseItemClickListener { course ->
            val bundle = Bundle().apply {
                putSerializable(Constants.COURSE_BUNDLE_KEY, course)
            }
            findNavController().navigate(
                R.id.action_searchCoursesFragment_to_coursesDetailFragment,
                bundle
            )
        }

        var job: Job? = null
        binding.searchInputEditText.editText?.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_COURSES_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchCourses("en", editable.toString())
                    }
                }
            }
        }

        viewModel.searchCourses.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { coursesResponse ->
                        coursesAdapter.differ.submitList(coursesResponse.results.toList())
                        val totalPages = coursesResponse.count / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchCoursesPage == totalPages
                        if (isLastPage) {
                            binding.searchCoursesRecyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        binding.errorMessageView.retryBottom.setOnClickListener {
            if (binding.searchInputEditText.editText?.text.toString().isNotEmpty()) {
                viewModel.searchCourses("en", binding.searchInputEditText.editText?.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        binding.errorMessageView.errorMessageCardView.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.errorMessageView.errorMessageCardView.visibility = View.VISIBLE
        binding.errorMessageView.errorMessageTextView.text = message
        isError = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.searchCourses(
                    "en",
                    binding.searchInputEditText.editText?.text.toString()
                )
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter()
        binding.searchCoursesRecyclerView.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchCoursesFragment.scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}