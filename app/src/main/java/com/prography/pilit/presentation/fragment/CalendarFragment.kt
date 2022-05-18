package com.prography.pilit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.prography.pilit.R
import com.prography.pilit.databinding.CalendarDayLayoutBinding
import com.prography.pilit.databinding.FragmentCalendarBinding
import com.prography.pilit.databinding.ItemCalendarHeaderBinding
import com.prography.pilit.presentation.adapter.CalendarRecordAdapter
import com.prography.pilit.presentation.makeInVisible
import com.prography.pilit.presentation.makeVisible
import com.prography.pilit.presentation.setTextColorRes
import com.prography.pilit.presentation.viewmodel.CalendarViewModel
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.showAlignBottom
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding ?: throw IllegalArgumentException("Must be initialized.")
    private val today = LocalDate.now()

    private val viewModel by viewModels<CalendarViewModel>()

    private var selectedDate: LocalDate? = null

    private val adapter by lazy {
        CalendarRecordAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCalendarBinding.bind(view)

        initRecyclerView()

        val balloon = Balloon.Builder(requireContext())
            .setLayout(R.layout.calendar_help_balloon)
            .setArrowSize(0)
            .setBackgroundDrawableResource(R.drawable.bg_calendar_tooltip)
            .setArrowOrientation(ArrowOrientation.BOTTOM)
            .setBackgroundColorResource(R.color.transparent)
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        val imageView = binding.ivTooltip
        imageView.setOnClickListener {
            it.showAlignBottom(balloon)
            if (balloon.isShowing) {
                balloon.dismiss()
            } else {
                balloon.showAlignBottom(it)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
            val dotView = CalendarDayLayoutBinding.bind(view).exThreeDotView
            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = ItemCalendarHeaderBinding.bind(view).legendLayout.root
        }


        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
        viewModel.getMonthlyPillList(currentMonth.year, currentMonth.month.value)

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                val textView = container.textView
                val dotView = container.dotView

                textView.text = day.date.dayOfMonth.toString()

                viewModel.monthlyPillListData.observe(viewLifecycleOwner) {
                    it.forEach { takeLog ->
                        if (day.owner == DayOwner.THIS_MONTH) {
                            textView.makeVisible()
                            val date = LocalDate.parse(takeLog.eatDate, DateTimeFormatter.ISO_DATE)
                            when (date) {
                                today -> {
                                    textView.setTextColorRes(R.color.white)
                                    dotView.makeVisible()
                                }
                                else -> {
                                    dotView.makeInVisible()
                                    textView.background = null
                                    when (takeLog.pillState) {
                                        0 -> {
                                            textView.setTextColorRes(R.color.black)
                                            textView.setBackgroundResource(R.drawable.bg_pill_part_eaten)
                                        }
                                        1 -> {
                                            textView.setTextColorRes(R.color.white)
                                            textView.setBackgroundResource(R.drawable.bg_pill_all_eaten)
                                        }
                                        else -> {
                                            textView.setTextColorRes(R.color.black)
                                            textView.setBackgroundResource(R.color.transparent)
                                        }
                                    }
                                }
                            }
                        } else {
                            textView.makeInVisible()
                            dotView.makeInVisible()
                        }
                    }
                }
            }
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                    }
                }
            }

        viewModel.alertListData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)
//            updateAdapterForDate(date)
        }
    }
//    private fun updateAdapterForDate(date: LocalDate) {
//        eventsAdapter.apply {
//            events.clear()
//            events.addAll(this@Example3Fragment.events[date].orEmpty())
//            notifyDataSetChanged()
//        }
//        binding.exThreeSelectedDateText.text = selectionFormatter.format(date)
//    }
}

