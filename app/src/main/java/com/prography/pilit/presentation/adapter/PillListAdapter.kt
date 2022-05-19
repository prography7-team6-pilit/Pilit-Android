package com.prography.pilit.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prography.pilit.databinding.ItemPillListBinding
import com.prography.pilit.domain.model.Pill

class PillListAdapter(
    private val onPillClicked: (alertId: Int) -> Unit,
    private val onOptionClicked: (alertId: Int) -> Unit
) : ListAdapter<Pill, PillListAdapter.ViewHolder>(pillComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder =
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
        private val onOptionClicked: (alertId: Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pill) {
            binding.pill = item
            binding.ivPillListTaking.setOnClickListener {
                onPillClicked(item.alertId)
            }
            binding.ivPillListOption.setOnClickListener{
                onOptionClicked(item.alertId)
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