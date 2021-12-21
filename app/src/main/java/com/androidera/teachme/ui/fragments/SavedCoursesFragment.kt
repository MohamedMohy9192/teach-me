package com.androidera.teachme.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidera.teachme.R
import com.androidera.teachme.adapters.CoursesAdapter
import com.androidera.teachme.databinding.FragmentSavedCoursesBinding
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Constants.Companion.COURSE_BUNDLE_KEY
import com.google.android.material.snackbar.Snackbar

class SavedCoursesFragment : Fragment() {

    private var _binding: FragmentSavedCoursesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CoursesViewModel
    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedCoursesBinding.inflate(inflater, container, false)
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
                R.id.action_savedCoursesFragment_to_coursesDetailFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val course = coursesAdapter.differ.currentList[position]
                viewModel.deleteCourse(course)
                Snackbar.make(view, "Successfully deleted course", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveCourse(course)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedCoursesRecyclerView)
        }
        viewModel.getSavedCourses().observe(viewLifecycleOwner, Observer { courses ->
            coursesAdapter.differ.submitList(courses)
        })
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter()
        binding.savedCoursesRecyclerView.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}