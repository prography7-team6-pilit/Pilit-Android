package com.prography.pilit.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.pilit.R
import com.prography.pilit.databinding.ItemPillListBinding
import com.prography.pilit.domain.model.Pill
import java.text.SimpleDateFormat
import java.util.*

class PillListAdapter(
    private val onPillClicked: (alertId: Int) -> Unit,
    private val onOptionClicked: (pillData: Pill, selectedMenuNum: Int) -> Unit
) : ListAdapter<Pill, PillListAdapter.ViewHolder>(pillComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemPillListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPillClicked,
            onOptionClicked
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem((position)))
    }

    inner class ViewHolder(
        private val binding: ItemPillListBinding,
        private val onPillClicked: (alertId: Int) -> Unit,
        private val onOptionClicked: (pillData: Pill, selectedMenuNum: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pill) {
            val currentTime = Calendar.getInstance()
            currentTime.time = Date()
            currentTime.add(Calendar.MINUTE, -30)
            val currentDate = Date(currentTime.timeInMillis)
            val currentDateFormat = SimpleDateFormat("HH:mm").format(currentDate)
            val currentDateConvert = SimpleDateFormat("HH:mm").parse(currentDateFormat)
            var mainTime = item.alertTime.last()
            for (i in 0 until item.alertTime.size) {
                val alertDateTime = SimpleDateFormat("HH:mm").parse(item.alertTime[i])
                if (alertDateTime.after(currentDateConvert)) {
                    mainTime = item.alertTime[i]
                    break
                }
            }

            val mainTime24 = SimpleDateFormat("HH:mm").parse(mainTime)
            val am_pm = SimpleDateFormat("a").format(mainTime24) != "오전"
            val mainTime12 = SimpleDateFormat("hh:mm").format(mainTime24)

            binding.pill = item
            binding.amPm = am_pm
            binding.alertTime = mainTime12

            binding.ivPillListTaking.setOnClickListener {
                onPillClicked(item.alertId)
            }

            binding.ivPillListOption.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_pill_option, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.item_pill_edit -> {
                            onOptionClicked(item, 0)
                            true
                        }
                        R.id.item_pill_delete -> {
                            onOptionClicked(item, 1)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    companion object {
        val pillComparator = object : DiffUtil.ItemCallback<Pill>() {
            override fun areItemsTheSame(oldItem: Pill, newItem: Pill): Boolean =
                oldItem.pillName == newItem.pillName

            override fun areContentsTheSame(oldItem: Pill, newItem: Pill): Boolean =
                oldItem == newItem
        }
    }
}