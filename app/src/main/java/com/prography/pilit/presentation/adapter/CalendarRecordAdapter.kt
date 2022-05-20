package com.prography.pilit.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.prography.pilit.R
import com.prography.pilit.databinding.ItemCalendarRecordBinding
import com.prography.pilit.domain.model.Pill

class CalendarRecordAdapter(
    private val onPillClicked: (alertId: Int) -> Unit
) : ListAdapter<Pill, CalendarRecordAdapter.ViewHolder>(pillComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder =
        ViewHolder(
            ItemCalendarRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onPillClicked
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem((position)))
    }

    inner class ViewHolder(
        private val binding: ItemCalendarRecordBinding,
        private val onPillClicked: (alertId: Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pill) {
            binding.pill = item
            binding.btnEat.setOnClickListener {
                binding.pill = item.copy(isEaten = binding.pill!!.isEaten.not())
                onPillClicked(item.id)
            }
        }
    }

    companion object {
        val pillComparator = object: DiffUtil.ItemCallback<Pill>() {
            override fun areItemsTheSame(oldItem: Pill, newItem: Pill): Boolean =
                oldItem.pillName == newItem.pillName

            override fun areContentsTheSame(oldItem: Pill, newItem: Pill): Boolean =
                oldItem == newItem
        }
    }
}