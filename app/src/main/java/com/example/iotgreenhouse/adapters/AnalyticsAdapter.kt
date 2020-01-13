package com.example.iotgreenhouse.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.iotgreenhouse.fragments.WeakelyFragment

class AnalyticsAdapter (
    private val context: Context,
    fragment: FragmentManager,
    internal var totalTabs: Int
): FragmentPagerAdapter(fragment) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                return WeakelyFragment()
            }
            else -> {
                return WeakelyFragment()
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}