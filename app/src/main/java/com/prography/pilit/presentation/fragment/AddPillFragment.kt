package com.prography.pilit.presentation.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prography.pilit.R
import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.databinding.FragmentAddPillBinding
import com.prography.pilit.domain.model.Week
import com.prography.pilit.presentation.activity.MainActivity
import com.prography.pilit.presentation.viewmodel.AddPillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddPillFragment : Fragment() {

    private var _binding: FragmentAddPillBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddPillViewModel>()
    private var alertTime24 = "08:00"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCompleteButtonActivation()
        setCompleteButtonClickListener()
        setTimeSettingButtonClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun initAddPillView(){
        binding.etAddPillMedicineName.text = Editable.Factory.getInstance().newEditable("")
        alertTime24 = "08:00"
        binding.tvAddPillIntakeTimeAlarmAmPm.text = getString(R.string.add_pill_default_alarm_am_pm)
        binding.tvAddPillIntakeTimeAlarmTime.text = getString(R.string.add_pill_default_alarm_time)
        binding.cbAddPillRepetitionOptionSun.isChecked = true
        binding.cbAddPillRepetitionOptionMon.isChecked = true
        binding.cbAddPillRepetitionOptionTue.isChecked = true
        binding.cbAddPillRepetitionOptionWed.isChecked = true
        binding.cbAddPillRepetitionOptionThu.isChecked = true
        binding.cbAddPillRepetitionOptionFri.isChecked = true
        binding.cbAddPillRepetitionOptionSat.isChecked = true
        binding.switchAddPillPush.isChecked = true
    }

    fun setCompleteButtonActivation(){
        binding.etAddPillMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                when {
                    s.toString().trim().isEmpty() -> {
                        binding.btnAddPillComplete.isEnabled = false
                    }
                    else -> {
                        binding.btnAddPillComplete.isEnabled = true
                    }
                }
            }
        })
    }

    fun getCheckedDay():List<Week>{
        val alertWeek:MutableList<Week> = mutableListOf()
        if(binding.cbAddPillRepetitionOptionSun.isChecked) alertWeek.add(Week.Sun)
        if(binding.cbAddPillRepetitionOptionMon.isChecked) alertWeek.add(Week.Mon)
        if(binding.cbAddPillRepetitionOptionTue.isChecked) alertWeek.add(Week.Tue)
        if(binding.cbAddPillRepetitionOptionWed.isChecked) alertWeek.add(Week.Wed)
        if(binding.cbAddPillRepetitionOptionThu.isChecked) alertWeek.add(Week.Thu)
        if(binding.cbAddPillRepetitionOptionFri.isChecked) alertWeek.add(Week.Fri)
        if(binding.cbAddPillRepetitionOptionSat.isChecked) alertWeek.add(Week.Sat)
        return alertWeek.toList()
    }

    fun setTimeSettingButtonClickListener(){
        binding.ivAddPillIntakeTimeSetting.setOnClickListener {
            val originTime = alertTime24.split(":")
            TimePickerDialog(requireActivity(), { view, hour, minute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                alertTime24 = SimpleDateFormat("HH:mm").format(cal.time)
                binding.tvAddPillIntakeTimeAlarmAmPm.text = if(hour < 12) "오전" else "오후"
                binding.tvAddPillIntakeTimeAlarmTime.text = SimpleDateFormat("hh:mm").format(cal.time)
            }, originTime[0].toInt(), originTime[1].toInt(), false).show()
        }
    }

    fun setCompleteButtonClickListener(){
        binding.btnAddPillComplete.setOnClickListener {
            var alertTime = alertTime24
            var alertWeek = getCheckedDay()
            val isPush = binding.switchAddPillPush.isChecked
            val pillName = binding.etAddPillMedicineName.text.toString()
            val body = AddAlertRequest(alertTime, alertWeek, isPush, pillName)
            viewModel.requestAddAlert(body = body)
            viewModel.addAlertSuccess.observe(viewLifecycleOwner){
                if(it) {
                    initAddPillView()
                    (requireActivity() as MainActivity).moveToFragment(0)
                }
            }
        }
    }
}