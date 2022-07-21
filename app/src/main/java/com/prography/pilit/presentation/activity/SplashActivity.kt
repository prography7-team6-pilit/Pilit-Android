package com.prography.pilit.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.prography.pilit.PilitApplication
import com.prography.pilit.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    private val splashViewTime: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collectFlow()
        lifecycleScope.launch {
            delay(splashViewTime)
            viewModel.login(Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID))
        }
    }

    private fun collectFlow() {
        lifecycleScope.launchWhenCreated {
            viewModel.isLoggedIn.collectLatest {
                when (it) {
                    true -> {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAfterTransition()
                    }
                    false -> {
                        val intent = Intent(this@SplashActivity, JoinActivity::class.java)
                        startActivity(intent)
                        finishAfterTransition()
                    }
                    else -> Unit
                }
            }
        }
    }




}