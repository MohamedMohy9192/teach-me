package com.androidera.teachme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.androidera.teachme.databinding.FragmentCoursesViewBinding

class CoursesViewFragment : Fragment() {
    private var _binding: FragmentCoursesViewBinding? = null
    private val binding get() = _binding!!
    val TAG = CoursesViewFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseUrl = "https://www.udemy.com${arguments?.getString("courseUrl")}"
        Log.d(TAG, "fullCourseUrl: $courseUrl")
        binding.coursesWebView.apply {
            webViewClient = WebViewClient()
            loadUrl(courseUrl)

        }
    }

}