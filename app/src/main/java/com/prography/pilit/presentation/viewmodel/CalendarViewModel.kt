package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.pill.Alert
import com.prography.pilit.data.datasource.remote.pill.TakeLog
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.usecase.RequestAlertUseCase
import com.prography.pilit.domain.usecase.RequestMonthlyPillUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val requestMonthlyPillUseCase: RequestMonthlyPillUseCase,
    private val requestAlertUseCase: RequestAlertUseCase
) : ViewModel() {

    val monthlyPillListData: MutableLiveData<List<TakeLog>> = MutableLiveData()
    val alertListData: MutableLiveData<List<Pill>> = MutableLiveData()

    fun getMonthlyPillList(year: Int, month: Int) {
        viewModelScope.launch {
            monthlyPillListData.postValue(requestMonthlyPillUseCase(year, month))
        }
    }

    fun getAlertLIst(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            alertListData.postValue(requestAlertUseCase(year, month, day))
        }
    }
}