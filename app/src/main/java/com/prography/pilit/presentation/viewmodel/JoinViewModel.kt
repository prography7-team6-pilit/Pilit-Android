package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.FirebaseService
import com.prography.pilit.data.datasource.remote.user.UserRequest
import com.prography.pilit.domain.model.User
import com.prography.pilit.domain.usecase.RequestJoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel@Inject constructor(
    private val requestJoinUseCase: RequestJoinUseCase
) : ViewModel() {

    lateinit var userInfo: User
    val joinSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val fcmToken: MutableLiveData<String> = MutableLiveData()

    fun requestJoin(uuid: String, nickname:String, firebasetoken:String) = viewModelScope.launch {
        val value = requestJoinUseCase(UserRequest(uuid=uuid, nickname = nickname, firebasetoken = firebasetoken))
        if(value.accessToken!=null) {
            userInfo = value
            joinSuccess.postValue(true)
        }
        else {
            joinSuccess.postValue(false)
        }
    }

    fun requestCurrentToken() = viewModelScope.launch {
        val result = FirebaseService().getCurrentToken()
        fcmToken.postValue(result)
    }
}