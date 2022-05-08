package com.prography.pilit.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prography.pilit.databinding.FragmentAddPillBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPillFragment : Fragment() {

    private var _binding: FragmentAddPillBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}