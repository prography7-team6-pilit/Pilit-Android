package com.prography.pilit.presentation.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.prography.pilit.R
import com.prography.pilit.data.datasource.remote.Resource
import com.prography.pilit.data.datasource.remote.pill.AddAlertRequest
import com.prography.pilit.databinding.FragmentAddPillBinding
import com.prography.pilit.domain.model.Week
import com.prography.pilit.presentation.activity.MainActivity
import com.prography.pilit.presentation.adapter.IntakeTimeListAdapter
import com.prography.pilit.presentation.viewmodel.AddPillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddPillFragment : Fragment() {

    private var _binding: FragmentAddPillBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddPillViewModel>()
    lateinit var weekList: List<CheckBox>
    private val intakeTimeListAdapter by lazy { IntakeTimeListAdapter(this::setAlertTimeClicked) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAddPillView()
        setDosage()
        setAlertCount()
        setIntakeTimeAdapter()
        setIntakeTimeCountClickListener()
        setCompleteButtonActivation()
        setCompleteButtonClickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDosage()
        viewModel.initIntakeCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initAddPillView() {
        weekList = listOf(
            binding.cbAddPillRepetitionOptionSun,
            binding.cbAddPillRepetitionOptionMon,
            binding.cbAddPillRepetitionOptionTue,
            binding.cbAddPillRepetitionOptionWed,
            binding.cbAddPillRepetitionOptionThu,
            binding.cbAddPillRepetitionOptionFri,
            binding.cbAddPillRepetitionOptionSat
        )

        binding.etAddPillMedicineName.text = Editable.Factory.getInstance().newEditable("")
        binding.switchAddPillPush.isChecked = true
        weekList.forEach { it.isChecked = true }
    }

    private fun setDosage(){
        viewModel.dosage.observe(viewLifecycleOwner){
            binding.tvAddPillIntakeAmountCount.text = it.toString()
        }

        binding.btnAddPillIntakeAmountSmaller.setOnClickListener {
            if(viewModel.dosage.value!! > 1) viewModel.minusDosage()
        }

        binding.btnAddPillIntakeAmountBigger.setOnClickListener {
            if(viewModel.dosage.value!! < 10) viewModel.plusDosage()
        }
    }

    private fun setAlertCount(){
        viewModel.intakeCount.observe(viewLifecycleOwner){
            binding.tvAddPillIntakeTimeCount.text = it.toString() + getString(R.string.count)
            val intakeTimeSpan = binding.tvAddPillIntakeTimeCount.text as Spannable
            intakeTimeSpan.setSpan(UnderlineSpan(), 0, binding.tvAddPillIntakeTimeCount.text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun setIntakeTimeAdapter(){
        binding.rvAddPillIntakeTime.adapter = intakeTimeListAdapter
        viewModel.intakeTimeList.observe(viewLifecycleOwner){
            intakeTimeListAdapter.submitList(it)
        }
    }

    private fun setAlertTimeClicked(index: Int){
       val originTime = viewModel.intakeTimeList.value!![index].split(":")
        TimePickerDialog(requireActivity(), { _, hour, minute ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            viewModel.setIntakeTime(index = index, intakeTime = SimpleDateFormat("HH:mm").format(cal.time))
            intakeTimeListAdapter.notifyItemChanged(index)
        }, originTime[0].toInt(), originTime[1].toInt(), false).show()
    }

    private fun setIntakeTimeCountClickListener(){
        binding.tvAddPillIntakeTimeCount.setOnClickListener {
            val numberPickerLayout = layoutInflater.inflate(R.layout.dialog_number_picker,null)
            val numberPicker = numberPickerLayout.findViewById<NumberPicker>(R.id.number_picker)
            val numberPickerArray = Array(10) { (it + 1).toString() }
            numberPicker?.wrapSelectorWheel = false
            numberPicker?.displayedValues = numberPickerArray
            numberPicker?.minValue = 1
            numberPicker?.maxValue = 10
            numberPicker?.value = viewModel.intakeCount.value!!

            val numberPickerDialog = AlertDialog.Builder(requireContext()).apply {
                setView(numberPickerLayout)
            }.create()
            numberPickerDialog.show()

            val tvNumberPickerConfirm = numberPickerLayout.findViewById<TextView>(R.id.tv_number_picker_confirm)
            tvNumberPickerConfirm?.setOnClickListener {
                if(numberPicker?.value!=null) viewModel.setIntakeCount(numberPicker.value)
                numberPickerDialog.dismiss()
            }

            viewModel.intakeCount.observe(viewLifecycleOwner){
                viewModel.setIntakeTimeSize(it)
                setIntakeTimeAdapter()
            }
        }
    }

    private fun setCompleteButtonActivation() {
        binding.etAddPillMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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

    private fun getCheckedDay(): List<Week> {
        val alertWeek = mutableListOf<Week>()
        weekList.forEachIndexed { index, checkBox -> if (checkBox.isChecked) alertWeek.add(Week.values()[index]) }
        return alertWeek
    }

    private fun setCompleteButtonClickListener() {
        binding.btnAddPillComplete.setOnClickListener {
            val alertTime = viewModel.intakeTimeList.value?.toList()
            val alertWeek = getCheckedDay()
            val isPush = binding.switchAddPillPush.isChecked
            val dosage = viewModel.dosage.value
            val pillName = binding.etAddPillMedicineName.text.toString()
            val body = AddAlertRequest(alertTime = alertTime, alertWeek = alertWeek, isPush = isPush, dosage = dosage!!, pillName = pillName)
            viewModel.requestAddAlert(body = body)
            viewModel.addAlertSuccess.observe(viewLifecycleOwner) {
                when(it){
                    is Resource.Success -> {
                        initAddPillView()
                        (requireActivity() as MainActivity).moveToFragment(R.id.pillListFragment)
                    }
                    is Resource.Error -> {

                    }
                    else -> {}
                }
            }
        }
    }
}