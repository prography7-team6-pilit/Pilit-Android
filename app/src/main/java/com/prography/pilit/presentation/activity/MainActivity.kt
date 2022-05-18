package com.prography.pilit.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.prography.pilit.R
import com.prography.pilit.presentation.adapter.ViewPagerAdapter
import com.prography.pilit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.viewpager.adapter = ViewPagerAdapter(this)

        mainBinding.viewpager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mainBinding.bottomNavigation.menu.getItem(position).isChecked = true
                }
            }
        )

        mainBinding.bottomNavigation.itemIconTintList = null
        mainBinding.bottomNavigation.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_pill_list -> {
                        mainBinding.viewpager.currentItem = 0
                        true
                    }
                    R.id.item_pill_add -> {
                        mainBinding.viewpager.currentItem = 1
                        true
                    }
                    R.id.item_calendar -> {
                        mainBinding.viewpager.currentItem = 2
                        true
                    }
                    else -> false
                }
            }
        }
    }

    fun moveToFragment(fragmentIndex: Int) {
        mainBinding.viewpager.currentItem = fragmentIndex
    }
}