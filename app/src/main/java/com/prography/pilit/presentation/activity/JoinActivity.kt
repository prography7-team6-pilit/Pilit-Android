package com.prography.pilit.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.prography.pilit.R
import com.prography.pilit.databinding.ActivityJoinBinding
import com.prography.pilit.presentation.viewmodel.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    private val viewModel by viewModels<JoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.background_orange)
        collectFlow()
        verifyJoinNickname()
        setConfirmButtonClickListener()
    }

    private fun collectFlow() {
        lifecycleScope.launchWhenCreated {
            viewModel.isLoggedIn.collectLatest {
                if (it) {
                    moveToMain()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.errorMessage.collectLatest {
                if (it.isBlank()) {
                    binding.tilJoinNickname.error = null
                } else {
                    binding.tilJoinNickname.error = it
                }
            }
        }

    }

    private fun verifyJoinNickname() {
        binding.etJoinNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilJoinNickname.error = null
            }

            override fun afterTextChanged(s: Editable?) {
                when {
                    s.toString().trim().isEmpty() -> {
                        binding.btnJoinConfirm.isEnabled = false
                        binding.tilJoinNickname.error = getString(R.string.join_nickname_empty)
                    }
                    else -> {
                        binding.btnJoinConfirm.isEnabled = true
                        binding.tilJoinNickname.error = null
                    }
                }
            }
        })
    }

    private fun setConfirmButtonClickListener() {
        binding.btnJoinConfirm.setOnClickListener {
            viewModel.requestCurrentToken()
            viewModel.fcmToken.observe(this) {
                viewModel.requestJoin(
                    uuid = Settings.Secure.getString(this.applicationContext.contentResolver, Settings.Secure.ANDROID_ID),
                    nickname = binding.etJoinNickname.text?.trim().toString(),
                    firebasetoken = it
                )
            }
        }
    }

    private fun moveToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }
}