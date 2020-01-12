package com.example.iotgreenhouse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.activity_analytics.*

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        tabs_holder.addTab(tabs_holder.newTab().setText(R.string.daily))
    }
}
