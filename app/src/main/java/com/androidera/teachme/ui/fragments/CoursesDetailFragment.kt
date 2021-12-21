package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidera.teachme.R
import com.androidera.teachme.databinding.FragmentCoursesDetailBinding

class CoursesDetailFragment : Fragment() {
    private var _binding: FragmentCoursesDetailBinding? = null
    private val binding get() = _binding!!
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
        val course = args.course

        binding.openCoursesViewFragmentButton.setOnClickListener {
            val bundle = bundleOf("courseUrl" to course.url)
            findNavController().navigate(
                R.id.action_coursesDetailFragment_to_coursesViewFragment,
                bundle
            )
        }

    }
}