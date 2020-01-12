package com.example.iotgreenhouse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_navigation.setOnItemSelectedListener {
            when (it) {
                R.id.analytics -> {
                    val analyticsActivity = Intent(this, AnalyticsActivity::class.java)
                    startActivity(analyticsActivity)
                }
                R.id.notifications -> Toast.makeText(applicationContext, "ALERT", Toast.LENGTH_SHORT).show()
                R.id.home -> Toast.makeText(applicationContext, "HOME", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
