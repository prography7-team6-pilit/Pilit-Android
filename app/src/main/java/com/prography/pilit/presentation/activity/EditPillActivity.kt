package com.prography.pilit.presentation.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.databinding.ActivityEditPillBinding
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.domain.model.Week
import com.prography.pilit.presentation.viewmodel.EditPillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditPillActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditPillBinding
    private val viewModel by viewModels<EditPillViewModel>()
    private lateinit var pillData: Pill
    private lateinit var alertTime24: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditPillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        pillData = intent.getParcelableExtra("pillData")!!
        binding.pill = pillData

        setBackButtonClickListener()
        setCompleteButtonActivation()
        setCompleteButtonClickListener()
        setTimeSettingButtonClickListener()
    }

    fun convert12hourTo24hour(am_pm:Boolean, alertTime:String) : String{
        val cal = Calendar.getInstance()
        cal.set(Calendar.AM_PM, if(!am_pm) 0 else 1 )
        cal.set(Calendar.HOUR, alertTime.split(":")[0].toInt())
        cal.set(Calendar.MINUTE, alertTime.split(":")[1].toInt())
        return SimpleDateFormat("HH:mm").format(cal.time)
    }

    fun setCompleteButtonActivation(){
        binding.etEditPillMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                when {
                    s.toString().trim().isEmpty() -> {
                        binding.btnEditPillComplete.isEnabled = false
                    }
                    else -> {
                        binding.btnEditPillComplete.isEnabled = true
                    }
                }
            }
        })
    }

    private fun getCheckedDay():List<Week>{
        val alertWeek:MutableList<Week> = mutableListOf()
        if(binding.cbEditPillRepetitionOptionSun.isChecked) alertWeek.add(Week.Sun)
        if(binding.cbEditPillRepetitionOptionMon.isChecked) alertWeek.add(Week.Mon)
        if(binding.cbEditPillRepetitionOptionTue.isChecked) alertWeek.add(Week.Tue)
        if(binding.cbEditPillRepetitionOptionWed.isChecked) alertWeek.add(Week.Wed)
        if(binding.cbEditPillRepetitionOptionThu.isChecked) alertWeek.add(Week.Thu)
        if(binding.cbEditPillRepetitionOptionFri.isChecked) alertWeek.add(Week.Fri)
        if(binding.cbEditPillRepetitionOptionSat.isChecked) alertWeek.add(Week.Sat)
        return alertWeek.toList()
    }

    private fun setTimeSettingButtonClickListener(){
        binding.ivEditPillIntakeTimeSetting.setOnClickListener {
            val originTime = alertTime24.split(":")
            TimePickerDialog(this, { view, hour, minute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                alertTime24 = SimpleDateFormat("HH:mm").format(cal.time)
                binding.tvEditPillIntakeTimeAlarmAmPm.text = if(hour < 12) "오전" else "오후"
                binding.tvEditPillIntakeTimeAlarmTime.text = SimpleDateFormat("hh:mm").format(cal.time)
            }, originTime[0].toInt(), originTime[1].toInt(), false).show()
        }
    }

    private fun setCompleteButtonClickListener(){
        binding.btnEditPillComplete.setOnClickListener {
            var alertTime = alertTime24
            var alertWeek = getCheckedDay()
            val isPush = binding.switchEditPillPush.isChecked
            val pillName = binding.etEditPillMedicineName.text.toString()
            val body = AddAlertRequest(alertTime, alertWeek, isPush, pillName)
            viewModel.requestEditAlert(alertId = pillData.alertId, body = body)
            viewModel.editAlertSuccess.observe(this){
                if(it) this.finish()
            }
        }
    }

    private fun setBackButtonClickListener(){
        binding.ivEditPillBack.setOnClickListener {
            this.finish()
        }
    }
}