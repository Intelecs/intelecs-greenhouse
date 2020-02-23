package com.example.iotgreenhouse.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iotgreenhouse.Network.WebSocketClient
import com.example.iotgreenhouse.Network.WebSocketNetwork
import com.example.iotgreenhouse.R
import com.example.iotgreenhouse.model.SendControls
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tinder.scarlet.websocket.WebSocketEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {

    var client: OkHttpClient = OkHttpClient.Builder().build()
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val request = Request.Builder().url("ws://192.168.43.66:8001/control-devices").build()
        val sensorListner = Request.Builder().url("ws://192.168.43.66:8001/sensors").build()

        val webSocketListener = WebSocketNetwork()
        val webSocket = client.newWebSocket(request, webSocketListener)
        val sensorClient = client.newWebSocket(sensorListner, webSocketListener)
        client.dispatcher.executorService.execute {

        }
        Log.d("Websocke", "Request ${webSocket.request().body}")

//        val sendControls = SendControls("FAN", "ON")
//
//        WebSocketClient.iotService.observeNetwork().filter {
//            it is WebSocketEvent.OnConnectionOpened
//        }.subscribe{
//            Log.d("Message", "Connected $it")
//        }
//
//
//        val subscribe = WebSocketClient.iotService.obeserveTemperature().subscribe {
//            Log.d("Message", "Temperature ${it.reading} Date ${it.date_added}")
//        }

        fan_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                Log.d("Message", "${isChecked}")
                webSocket.send("hello")

//                WebSocketClient.iotService.observeNetwork().filter {
//                    it is WebSocketEvent.OnConnectionOpened
//                }.subscribe{ WebSocketClient.iotService.sendControl(sendControls)}
            }
        }

        connection_holder.setOnClickListener {
            val rotateAnimation = RotateAnimation(
                0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotateAnimation.setInterpolator(LinearInterpolator())
            refresh.startAnimation(rotateAnimation)
        }


        edit_threshold.setOnClickListener {
            val viewInflated: View =
                LayoutInflater.from(this).inflate(R.layout.threshold_layout, null)
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

        //connect()
    }
}
