package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.pill.EatRequest
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.usecase.RequestAlertUseCase
import com.prography.pilit.domain.usecase.RequestDeleteAlertUseCase
import com.prography.pilit.domain.usecase.postTakingLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PillListViewModel @Inject constructor(
    private val requestAlertUseCase: RequestAlertUseCase,
    private val postTakingLogsUseCase: postTakingLogsUseCase,
    private val requestDeleteAlertUseCase: RequestDeleteAlertUseCase
) : ViewModel() {

    val alertListData: MutableLiveData<List<Pill>> = MutableLiveData()
    val takingLogsData: MutableLiveData<Boolean> = MutableLiveData()
    val deleteAlertSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun getAlertList(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            alertListData.postValue(requestAlertUseCase(year, month, day))
        }
    }

    fun  postTakingLogs(alertId: Int) {
        viewModelScope.launch {
            takingLogsData.postValue(postTakingLogsUseCase(EatRequest(alertId)))
        }
    }

    fun requestDeleteAlert(alertId: Int) = viewModelScope.launch {
        val response = requestDeleteAlertUseCase(alertId = alertId)
        if(response.isSuccessful){
            deleteAlertSuccess.postValue(true)
        }
        else{
            deleteAlertSuccess.postValue(false)
        }
    }
}