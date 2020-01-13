package com.example.iotgreenhouse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotgreenhouse.R
import com.example.iotgreenhouse.adapters.AnalyticsAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_analytics.*

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        tabs_holder.addTab(tabs_holder.newTab().setText(R.string.daily))
        tabs_holder.addTab(tabs_holder.newTab().setText(R.string.weakly))
        tabs_holder.addTab(tabs_holder.newTab().setText(R.string.monthly))
        tabs_holder.addTab(tabs_holder.newTab().setText(R.string.yearly))


        tabs_holder.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = AnalyticsAdapter(this, supportFragmentManager, tabs_holder.tabCount)
        home_page_viewer.adapter  = adapter

        tabs_holder.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                home_page_viewer.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {}
        })
    }
}
