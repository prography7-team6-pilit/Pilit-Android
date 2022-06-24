package com.prography.pilit.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.domain.usecase.RequestAddAlertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPillViewModel @Inject constructor(
    private val requestAddAlertUseCase: RequestAddAlertUseCase
) : ViewModel() {

    private val _dosage: MutableLiveData<Int> = MutableLiveData()
    val dosage: LiveData<Int> get() = _dosage

    private val _intakeCount: MutableLiveData<Int> = MutableLiveData()
    val intakeCount: LiveData<Int> get() = _intakeCount

    private val _intakeTimeList: MutableLiveData<MutableList<String>> = MutableLiveData()
    val intakeTimeList: LiveData<MutableList<String>> get() = _intakeTimeList

    private val _addAlertSuccess: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val addAlertSuccess: LiveData<Resource<Boolean>> get() = _addAlertSuccess

    fun requestAddAlert(body: AddAlertRequest) = viewModelScope.launch {
        val response = requestAddAlertUseCase(body = body)
        if (response.result) {
            _addAlertSuccess.postValue(Resource.Success(response.result))
        } else {
            _addAlertSuccess.postValue(Resource.Error(response.result.toString()))
        }
    }

    fun setIntakeTimeSize(size: Int){
        _intakeTimeList.value?.let {
            if(it.size > size) {
                _intakeTimeList.value = _intakeTimeList.value?.take(size)?.toMutableList()
            }
            else{
                for (i in it.size until size) {
                    val cal = Calendar.getInstance()
                    val dataFormat = SimpleDateFormat("HH:mm")
                    val prevDate = dataFormat.parse(it[i-1])
                    cal.time = prevDate
                    cal.add(Calendar.HOUR_OF_DAY, 1)
                    _intakeTimeList.value?.add(dataFormat.format(cal.time))
                }
            }
        }
    }

    fun setIntakeTime(index: Int, intakeTime: String) {
        _intakeTimeList.value?.let{
            it[index] = intakeTime
        }
    }

    fun initDosage() {
        _dosage.value = 1
    }

    fun plusDosage() {
        _dosage.value = _dosage.value?.plus(1)
    }

    fun minusDosage() {
        _dosage.value = _dosage.value?.minus(1)
    }

    fun initIntakeCount() {
        _intakeCount.value = 1
        _intakeTimeList.value = mutableListOf("08:00")
    }

    fun setIntakeCount(count: Int) {
        _intakeCount.value = count
    }
}