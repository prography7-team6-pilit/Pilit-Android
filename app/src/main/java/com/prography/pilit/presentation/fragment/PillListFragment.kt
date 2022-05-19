package com.prography.pilit.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prography.pilit.PilitApplication
import com.prography.pilit.R
import com.prography.pilit.databinding.FragmentPillListBinding
import com.prography.pilit.presentation.activity.MainActivity
import com.prography.pilit.presentation.adapter.PillListAdapter
import com.prography.pilit.presentation.viewmodel.CalendarViewModel
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.showAlignBottom
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PillListFragment : Fragment() {

    private var _binding: FragmentPillListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CalendarViewModel>()
    private val pillListAdapter by lazy {
        PillListAdapter(this::eatPill, this::pillOption)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPillListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitUserInformation()
        setPillListAdapter()
        setAddPillButtonClickListener()
        setAlertData()
        setToolTip()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setAddPillButtonClickListener(){
        binding.btnPillListTodayPillZeroAdd.setOnClickListener {
            (requireActivity() as MainActivity).moveToFragment(1)
        }
    }

    fun setInitUserInformation(){
        binding.nickname = PilitApplication.preferences.nickname
    }

    fun setPillListAdapter(){
        binding.rvPillList.adapter = pillListAdapter
    }

    fun setAlertData(){
        val currentDay = Calendar.getInstance()
        val year = currentDay.get(Calendar.YEAR)
        val month = currentDay.get(Calendar.MONTH)
        val day = currentDay.get(Calendar.DAY_OF_MONTH)
        viewModel.getAlertList(year = year, month= month, day= day)
        viewModel.alertListData.observe(viewLifecycleOwner){
            binding.pillCount = it.size
            if(it.isNotEmpty()){
                pillListAdapter.submitList(it)
            }
        }
    }

    private fun eatPill(alertId: Int) {
        viewModel.postTakingLogs(alertId)
        viewModel.takingLogsData.observe(viewLifecycleOwner){
            setAlertData()
        }
    }

    private fun pillOption(alertId: Int) {

    }

    private fun setToolTip(){
        val balloon = Balloon.Builder(requireContext())
            .setLayout(R.layout.calendar_tooltip)
            .setArrowSize(0)
            .setBackgroundDrawableResource(R.drawable.bg_calendar_tooltip)
            .setArrowOrientation(ArrowOrientation.BOTTOM)
            .setBackgroundColorResource(R.color.transparent)
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        binding.ivPillListTooltip.setOnClickListener {
            it.showAlignBottom(balloon)
            if (balloon.isShowing) balloon.dismiss()
            else balloon.showAlignBottom(it)
        }
    }
}