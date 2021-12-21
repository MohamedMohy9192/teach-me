package com.androidera.teachme.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androidera.teachme.databinding.FragmentCoursesDetailBinding
import com.androidera.teachme.ui.CoursesActivity
import com.androidera.teachme.ui.CoursesViewModel
import com.androidera.teachme.util.Constants
import com.google.android.material.snackbar.Snackbar

class CoursesDetailFragment : Fragment() {
    private var _binding: FragmentCoursesDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CoursesViewModel
    val args: CoursesDetailFragmentArgs by navArgs()
    val TAG = CoursesDetailFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as CoursesActivity).viewModel
        val course = args.course

        binding.floatingActionButton.setOnClickListener {
            viewModel.saveCourse(course)
            Snackbar.make(view, "Course saved successfully", Snackbar.LENGTH_LONG).show()
        }

        binding.openCoursesViewFragmentButton.setOnClickListener {
            val webpage = Uri.parse(Constants.UDEMY_BASE_URL + course.url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }

        }

    }
}