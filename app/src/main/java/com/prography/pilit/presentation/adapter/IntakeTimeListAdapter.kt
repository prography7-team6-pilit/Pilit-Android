package com.prography.pilit.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.pilit.R
import com.prography.pilit.databinding.ItemIntakeTimeBinding
import java.text.SimpleDateFormat
import java.util.*

class IntakeTimeListAdapter(
    private val onTimeClicked: (index: Int) -> Unit
) : ListAdapter<String, IntakeTimeListAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder =
        ViewHolder(
            ItemIntakeTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onTimeClicked
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem((position)), position)
    }

    inner class ViewHolder(
        private val binding: ItemIntakeTimeBinding,
        private val onTimeClicked: (index: Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(intakeTime: String, position: Int) {
            val alertTime24 = SimpleDateFormat("HH:mm", Locale.KOREA).parse(intakeTime)
            val am_pm = SimpleDateFormat("a", Locale.KOREA).format(alertTime24) != binding.root.context.getString(R.string.am)
            val alertTime12 = SimpleDateFormat("hh:mm", Locale.KOREA).format(alertTime24)
            binding.amPm = am_pm
            binding.intakeTime = alertTime12
            binding.ivAddPillIntakeTimeSetting.setOnClickListener {
                onTimeClicked(position)
            }
        }
    }

    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}