package com.prography.pilit.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.prography.pilit.PilitApplication
import com.prography.pilit.R
import com.prography.pilit.databinding.FragmentPillListBinding
import com.prography.pilit.domain.model.Pill
import com.prography.pilit.presentation.activity.MainActivity
import com.prography.pilit.presentation.adapter.PillListAdapter
import com.prography.pilit.presentation.viewmodel.PillListViewModel
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
    private val viewModel by viewModels<PillListViewModel>()
    private val pillListAdapter by lazy {
        PillListAdapter(this::eatPill, this::pillOption)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPillListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitUserInformation()
        setPillListAdapter()
        setAddPillButtonClickListener()
        setToolTip()
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.statusBarColor = requireActivity().getColor(R.color.background_orange)
        setAlertData()
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.statusBarColor = requireActivity().getColor(R.color.transparent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setAddPillButtonClickListener() {
        binding.btnPillListTodayPillZeroAdd.setOnClickListener {
            (requireActivity() as MainActivity).moveToFragment(R.id.addPillFragment)
        }
    }

    private fun setInitUserInformation() {
        binding.nickname = PilitApplication.preferences.nickname
    }

    private fun setPillListAdapter() {
        binding.rvPillList.adapter = pillListAdapter
    }

    private fun setAlertData() {
        val currentDay = Calendar.getInstance()
        val year = currentDay.get(Calendar.YEAR)
        val month = currentDay.get(Calendar.MONTH)
        val day = currentDay.get(Calendar.DAY_OF_MONTH)
        viewModel.getAlertList(year = year, month = month + 1, day = day)
        viewModel.alertListData.observe(viewLifecycleOwner) {
            binding.pillCount = it.size
            if (it.isNotEmpty()) {
                pillListAdapter.submitList(it.sortedBy { alert -> alert.alertId })
            }
        }
    }

    private fun eatPill(alertId: Int) {
        viewModel.postTakingLogs(alertId)
        viewModel.takingLogsData.observe(viewLifecycleOwner) {
            setAlertData()
        }
    }

    private fun pillOption(pillData: Pill, selectedMenuNum: Int) {
        when (selectedMenuNum) {
            0 -> { // ??????
                Navigation.findNavController(requireView()).navigate(
                    PillListFragmentDirections.actionPillListFragmentToEditPillActivity(pillData = pillData)
                )
            }
            1 -> { // ??????
                // ???????????????
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("????????? ?????????????????????????")
                    setMessage("??????????????? ????????? ?????? ???????????????!")
                    setPositiveButton("??????") { dialog, _ ->
                        viewModel.requestDeleteAlert(alertId = pillData.alertId)
                        viewModel.deleteAlertSuccess.observe(viewLifecycleOwner) {
                            if (it) setAlertData()
                        }
                        dialog.dismiss()
                    }
                    setNegativeButton("??????") { dialog, _ ->
                        dialog.dismiss()
                    }
                }.create().show()
            }
        }
    }

    private fun setToolTip() {
        val balloon = Balloon.Builder(requireContext())
            .setLayout(R.layout.calendar_tooltip)
            .setArrowSize(0)
            .setBackgroundDrawableResource(R.drawable.bg_calendar_tooltip)
            .setArrowOrientation(ArrowOrientation.BOTTOM)
            .setBackgroundColorResource(R.color.transparent)
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .setLifecycleOwner(viewLifecycleOwner)
            .setElevation(10)
            .build()

        binding.ivPillListTooltip.setOnClickListener {
            it.showAlignBottom(balloon)
            if (balloon.isShowing) balloon.dismiss()
            else balloon.showAlignBottom(it)
        }
    }
}