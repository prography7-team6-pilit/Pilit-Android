package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.PilitApplication
import com.prography.pilit.data.datasource.remote.FirebaseService
import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.model.User
import com.prography.pilit.domain.usecase.RequestJoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val requestJoinUseCase: RequestJoinUseCase,
) : ViewModel() {

    private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    val fcmToken: MutableLiveData<String> = MutableLiveData()

    fun requestJoin(uuid: String, nickname: String, firebasetoken: String) = viewModelScope.launch {
        when (val value = requestJoinUseCase(UserRequest(uuid = uuid, nickname = nickname, firebasetoken = firebasetoken))) {
            is Resource.Success<User> -> {
                val user = value.data as? User
                PilitApplication.preferences.nickname = user?.nickname
                PilitApplication.preferences.accessToken = user?.accessToken
                _isLoggedIn.emit(true)

            }
            is Resource.Error -> {
                _isLoggedIn.emit(false)
                _errorMessage.emit(value.errorMessage ?: "")
            }
        }
    }

    fun requestCurrentToken() = viewModelScope.launch {
        val result = FirebaseService().getCurrentToken()
        fcmToken.postValue(result)
    }
}