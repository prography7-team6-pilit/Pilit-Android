package com.prography.pilit.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.prography.pilit.presentation.fragment.AddPillFragment
import com.prography.pilit.presentation.fragment.CalendarFragment
import com.prography.pilit.presentation.fragment.PillListFragment

class ViewPagerAdapter(fragmemt: FragmentActivity): FragmentStateAdapter(fragmemt) {
    override fun getItemCount(): Int = FRAGMENTS.size

    override fun createFragment(position: Int): Fragment = FRAGMENTS[position]

    companion object {
        private val FRAGMENTS = listOf(
            PillListFragment(),
            AddPillFragment(),
            CalendarFragment()
        )
    }
}