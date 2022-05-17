package com.prography.pilit.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.prography.pilit.databinding.ActivityJoinBinding
import com.prography.pilit.presentation.viewmodel.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    private val viewModel by viewModels<JoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.requestJoin(uuid = "test", nickname = "test", firebasetoken = "test")
    }
}