package com.prography.pilit.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.usecase.RequestJoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel@Inject constructor(
    private val requestJoinUseCase: RequestJoinUseCase
) : ViewModel() {

    private val _joinSuccess = MutableLiveData<Boolean>()
    val joinSuccess = _joinSuccess

    fun requestJoin(uuid: String, nickname:String, firebasetoken:String) = viewModelScope.launch {
        val value = requestJoinUseCase(UserRequest(uuid=uuid, nickname = nickname, firebasetoken = firebasetoken))
        if(value.accessToken!=null) _joinSuccess.postValue(true)
        else _joinSuccess.postValue(false)
    }

}