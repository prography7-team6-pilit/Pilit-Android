package com.prography.pilit.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.prography.pilit.databinding.ActivityEditPillBinding

class EditPillActivity : Fragment() {

    private var _binding: ActivityEditPillBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityEditPillBinding.inflate(inflater, container, false)

        setBackButtonClickListener()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setBackButtonClickListener(){
        binding.ivEditPillBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}