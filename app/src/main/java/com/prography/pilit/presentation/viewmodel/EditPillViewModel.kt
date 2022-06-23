package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.domain.usecase.RequestEditAlertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPillViewModel @Inject constructor(
    private val requestEditAlertUseCase: RequestEditAlertUseCase
) : ViewModel() {

    var dosage = 1
    val editAlertSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun requestEditAlert(alertId: Int, body: AddAlertRequest) = viewModelScope.launch {
        val response = requestEditAlertUseCase(alertId = alertId, body = body)
        if(response.result){
            editAlertSuccess.postValue(true)
        }
        else{
            editAlertSuccess.postValue(false)
        }
    }
}