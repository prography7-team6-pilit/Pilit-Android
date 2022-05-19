package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.domain.usecase.RequestAddAlertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPillViewModel @Inject constructor(
    private val requestAddAlertUseCase: RequestAddAlertUseCase
) : ViewModel() {

    val addAlertSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun requestAddAlert(body: AddAlertRequest) = viewModelScope.launch {
        val response = requestAddAlertUseCase(body = body)
        if(response.isSuccessful){
            addAlertSuccess.postValue(true)
        }
        else{
            addAlertSuccess.postValue(false)
        }
    }
}