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
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.prography.pilit.R
import com.prography.pilit.data.datasource.remote.pill.TakeLog
import com.prography.pilit.databinding.CalendarDayLayoutBinding
import com.prography.pilit.databinding.FragmentCalendarBinding
import com.prography.pilit.databinding.ItemCalendarHeaderBinding
import com.prography.pilit.presentation.activity.MainActivity
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
    private val viewModel by viewModels<CalendarViewModel>()

    private var selectedDate: LocalDate? = LocalDate.now()

    private val adapter by lazy {
        CalendarRecordAdapter(this::eatPill)
    }

    private val logList: MutableList<TakeLog> = mutableListOf()

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
            .setLayout(R.layout.calendar_tooltip)
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

        //viewModel.getAlertList()

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                val textView = container.textView
                val dotView = container.dotView
                container.day = day

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.date.dayOfMonth.toString()

                    if (day.date == selectedDate) {
                        dotView.makeVisible()
                    } else {
                        dotView.makeInVisible()
                    }

                    val takeLog = logList.find {
                        LocalDate.parse(
                            it.eatDate,
                            DateTimeFormatter.ISO_DATE
                        ) == day.date
                    }
                    if (takeLog != null) {
                        if (takeLog.pillState == 0) {
                            textView.setBackgroundResource(R.drawable.bg_pill_part_eaten)
                            textView.setTextColorRes(R.color.black)
                        } else {
                            textView.setBackgroundResource(R.drawable.bg_pill_all_eaten)
                            textView.setTextColorRes(R.color.white)
                        }
                    } else {
                        textView.background = null
                        textView.setTextColorRes(R.color.black)
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }


        viewModel.monthlyPillListData.observe(viewLifecycleOwner) {
            logList.clear()
            logList.addAll(it)
            binding.calendarView.notifyCalendarChanged()
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

        binding.ivNextMonth.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.calendarView.monthScrollListener = { month ->
            val title = String.format(
                getString(R.string.calendar_title_year_month),
                month.yearMonth.year,
                month.yearMonth.monthValue
            )
            binding.tvCalendarTitle.text = title
            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.calendarView.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
            selectDate(month.yearMonth.atDay(1))
        }

        binding.ivPreviousMonth.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
            }
        }

        binding.layoutEmpty.btnAddAlert.setOnClickListener {
            (requireActivity() as MainActivity).moveToFragment(1)
        }

        viewModel.alertListData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.layoutEmpty.root.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.layoutEmpty.root.visibility = View.GONE
                adapter.submitList(it)
            }
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
            updateAdapterForDate(date)
            binding.tvRecordDate.text = String.format(
                getString(R.string.calendar_record_date),
                date.monthValue,
                date.dayOfMonth
            )
        }
    }

    private fun eatPill(alertId: Int) {
        viewModel.postTakingLogs(alertId)
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        viewModel.getAlertList(
            date?.year ?: LocalDate.now().year,
            date?.monthValue ?: LocalDate.now().monthValue,
            date?.dayOfMonth ?: LocalDate.now().dayOfMonth
        )
        adapter.notifyItemRangeRemoved(0, adapter.itemCount)
    }
}

