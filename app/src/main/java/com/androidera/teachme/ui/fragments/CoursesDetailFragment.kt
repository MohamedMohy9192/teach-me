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
import com.androidera.teachme.util.Constants

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
            val webpage = Uri.parse(Constants.UDEMY_BASE_URL + course.url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(requireActivity().packageManager) != null){
                startActivity(intent)
            }

        }

    }
}