package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidera.teachme.R
import com.androidera.teachme.adapters.CoursesAdapter
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Resource

class CoursesListFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel
    //private var fragmentCoursesListBinding: FragmentCoursesListBinding? = null
    private lateinit var coursesAdapter: CoursesAdapter

    private val TAG = CoursesListFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val binding = FragmentCoursesListBinding.bind(view)
        //fragmentCoursesListBinding = binding

        viewModel = (activity as CoursesActivity).viewModel

        setupRecyclerView(view)
        Log.d(
            TAG,
            "CoursesListFragment"
        )

        viewModel.courses.observe(viewLifecycleOwner, Observer { response ->

            Log.d(
                TAG,
                "Response In CoursesListFragment: courses.observe ${response.data?.next}"
            )

            when (response) {
                is Resource.Success -> {
                    hideProgressBar(view)
                    response.data?.let { coursesResponse ->
                        Log.d(
                            TAG,
                            "Response In CoursesListFragment: courses.observe ${coursesResponse.next}"
                        )
                        coursesAdapter.differ.submitList(coursesResponse.results)


                    }
                }
                is Resource.Error -> {
                    hideProgressBar(view)
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar(view)
                }
            }
        })
    }

    private fun hideProgressBar(view: View) {
        view.findViewById<ProgressBar>(R.id.pagination_progress_bar)?.visibility = View.INVISIBLE
    }

    private fun showProgressBar(view: View) {
        view.findViewById<ProgressBar>(R.id.pagination_progress_bar)?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(view: View) {
        coursesAdapter = CoursesAdapter()
        view.findViewById<RecyclerView>(R.id.courses_recycler_view)?.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

  /*  override fun onDestroyView() {
        fragmentCoursesListBinding = null
        super.onDestroyView()
    }*/
}