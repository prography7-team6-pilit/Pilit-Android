package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.PilitApplication
import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.domain.model.User
import com.prography.pilit.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * {description}
 *
 * @author capri
 * @since 2022/07/21
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel(){

    private val _isLoggedIn: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    fun login(uuid: String) = viewModelScope.launch {
        when (val value = loginUseCase(uuid)) {
            is Resource.Success<User> -> {
                PilitApplication.preferences.nickname = value.data.nickname
                PilitApplication.preferences.accessToken = value.data.accessToken
                _isLoggedIn.emit(true)
            }
            is Resource.Error -> {
                _isLoggedIn.emit(false)
            }
        }
    }
}