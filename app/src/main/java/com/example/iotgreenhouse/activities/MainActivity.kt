package com.example.iotgreenhouse.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iotgreenhouse.Network.WebSocketClient
import com.example.iotgreenhouse.R
import com.example.iotgreenhouse.model.SendControls
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendControls = SendControls("FAN", "ON")
        WebSocketClient.iotService.sendControl(sendControls)

//        WebSocketClient.iotService.observeNetwork().filter {
//            it is WebSocketEvent.OnConnectionOpened
//        }.subscribe({
//            WebSocketClient.iotService.sendControl(sendControls)
//        })
//
//        WebSocketClient.iotService.observeNetwork().filter {
//            it is WebSocketEvent.OnConnectionOpened
//        }.subscribe({
//            WebSocketClient.iotService.sendControl(sendControls)
//        })

        WebSocketClient.iotService.obeserveTemperature()
            .subscribe({
                    Log.d("Message", "Temperature ${it.reading} Date ${it.date_added}")
                })


        edit_threshold.setOnClickListener {
            val viewInflated:View = LayoutInflater.from(this).inflate(R.layout.threshold_layout, null)
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setOnCancelListener {

            }
            bottomSheetDialog.setTitle(getString(R.string.threshold_dialog_title))
            bottomSheetDialog.setContentView(viewInflated)
            bottomSheetDialog.show()
        }

        stream_card.setOnClickListener {
            val streamActivity = Intent(this, StreamActivity::class.java)
            startActivity(streamActivity)
        }

        btn_navigation.setOnItemSelectedListener {
            when (it) {
                R.id.analytics -> {
                    val analyticsActivity = Intent(this, AnalyticsActivity::class.java)
                    startActivity(analyticsActivity)
                }
                R.id.notifications -> {
                    val notificationActivity = Intent(this, NotificationActivity::class.java)
                    startActivity(notificationActivity)
                }
                R.id.home -> Toast.makeText(applicationContext, "HOME", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
