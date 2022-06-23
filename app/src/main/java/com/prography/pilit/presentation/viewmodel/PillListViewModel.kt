package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.LiveData
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

    private val _alertListData: MutableLiveData<List<Pill>> = MutableLiveData()
    val alertListData: LiveData<List<Pill>> get() = _alertListData

    private val _takingLogsData: MutableLiveData<Boolean> = MutableLiveData()
    val takingLogsData: LiveData<Boolean> get() = _takingLogsData

    private val _deleteAlertSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val deleteAlertSuccess: LiveData<Boolean> get() = _deleteAlertSuccess

    fun getAlertList(year: Int, month: Int, day: Int) = viewModelScope.launch {
        val response = requestAlertUseCase(year, month, day)
        _alertListData.postValue(response)
    }

    fun  postTakingLogs(alertId: Int) = viewModelScope.launch {
        _takingLogsData.postValue(postTakingLogsUseCase(EatRequest(alertId)))
    }

    fun requestDeleteAlert(alertId: Int) = viewModelScope.launch {
        val response = requestDeleteAlertUseCase(alertId = alertId)
        if(response.isSuccessful){
            _deleteAlertSuccess.postValue(true)
        }
        else{
            _deleteAlertSuccess.postValue(false)
        }
    }
}