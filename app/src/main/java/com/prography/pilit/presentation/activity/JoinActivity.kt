package com.prography.pilit.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.prography.pilit.PilitApplication
import com.prography.pilit.R
import com.prography.pilit.databinding.ActivityJoinBinding
import com.prography.pilit.presentation.viewmodel.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    private val viewModel by viewModels<JoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.background_orange)
        autoLogin()
        verifyJoinNickname()
        setConfirmButtonClickListener()
    }

    private fun autoLogin(){
        if(PilitApplication.preferences.nickname!=null && PilitApplication.preferences.accessToken!=null){
            moveToMain()
        }
    }

    private fun verifyJoinNickname(){
        binding.etJoinNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilJoinNickname.error = null
            }
            override fun afterTextChanged(s: Editable?) {
                when {
                    s.toString().trim().isEmpty() -> {
                        binding.btnJoinConfirm.isEnabled = false
                    }
                    else -> {
                        binding.btnJoinConfirm.isEnabled = true
                    }
                }
            }
        })
    }

    private fun setConfirmButtonClickListener(){
        binding.btnJoinConfirm.setOnClickListener {
            viewModel.requestCurrentToken()
            viewModel.fcmToken.observe(this){
                viewModel.requestJoin(uuid = UUID.randomUUID().toString(), nickname = binding.etJoinNickname.text.toString(), firebasetoken = it)
            }
            viewModel.joinSuccess.observe(this) {
                if(it){
                    PilitApplication.preferences.nickname = binding.etJoinNickname.text.toString()
                    PilitApplication.preferences.accessToken = viewModel.userInfo.accessToken
                    moveToMain()
                }
                else{
                    binding.tilJoinNickname.error = getString(R.string.join_nickname_error)
                }
            }

        }
    }

    private fun moveToMain(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }
}