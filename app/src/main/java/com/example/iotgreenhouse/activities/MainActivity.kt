package com.example.iotgreenhouse.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.iotgreenhouse.Network.WebSocketNetwork
import com.example.iotgreenhouse.R
import com.example.iotgreenhouse.model.SensorMessage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.tinder.scarlet.Message
import com.tinder.scarlet.websocket.WebSocketEvent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class MainActivity : AppCompatActivity() {

    var client: OkHttpClient = OkHttpClient.Builder().build()
    var webSocket:WebSocket? = null
    var sensorClient:WebSocket? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val request = Request.Builder().url("ws://192.168.43.66:8001/control-devices").build()
        val sensorRequest = Request.Builder().url("ws://192.168.43.66:8001/sensors").build()

        val webSocketListener = WebSocketNetwork()
        webSocket = client.newWebSocket(request, webSocketListener)
        sensorClient = client.newWebSocket(sensorRequest, webSocketListener)


        webSocketListener.observe()
            .subscribe {
                when (it) {
                    is WebSocketEvent.OnConnectionOpened -> {
                        Log.d("WebSocket Open:", "$it")
                        runOnUiThread {
                            if (connection_refresh.isVisible) {
                                connection_refresh.visibility = View.GONE
                            }
                            connection_image.setColorFilter(applicationContext.resources.getColor(R.color.colorPrimary))
                            connection_hint.text = getText(R.string.connected)
                        }
                    }
                    is WebSocketEvent.OnMessageReceived -> {
                        var message: Message? = null
                        message = it.message

                        val textData  = when(message) {
                            is Message.Text -> message.value
                            is Message.Bytes -> String(message.value)
                        }
                        val messageJson = Gson().fromJson(textData, SensorMessage::class.java)
                        Log.d("WebSocket Message:", "Received:${messageJson.payload}, ${messageJson.topic}")
                        when(messageJson.topic) {
                            "IOT-GREENHOUSE/WATER_LEVEL" -> {
                                runOnUiThread {
                                    val progess: Float? = messageJson.payload.toFloatOrNull()
                                    level_reading.text = messageJson.payload

                                    if (progess != null) progressView.progress = progess
                                }
                            }

                            "IOT-GREENHOUSE/TEMPERATURE" -> {
                                runOnUiThread {
                                    val progess: Float? = messageJson.payload.toFloatOrNull()
                                    level_reading.text = messageJson.payload

                                }
                            }

                            "IOT-GREENHOUSE/MOISTURE_LEVEL" -> {
                                runOnUiThread {
                                    val progess: Float? = messageJson.payload.toFloatOrNull()
                                    level_reading.text = messageJson.payload

                                }
                            }
                        }
                    }
                    is WebSocketEvent.OnConnectionClosing -> {
                        Log.d("WebSocket Closing:", "$it")
                        runOnUiThread {
                            if (!connection_refresh.isVisible) {
                                connection_refresh.visibility = View.VISIBLE
                            }
                            connection_image.setColorFilter(applicationContext.resources.getColor(R.color.ligh_gray))
                            connection_hint.text = getText(R.string.not_connected)
                        }
                    }
                    is WebSocketEvent.OnConnectionClosed -> {
                        Log.d("WebSocket Closed:", "$it")
                        runOnUiThread {
                            if (!connection_refresh.isVisible) {
                                connection_refresh.visibility = View.VISIBLE
                            }

                            connection_image.setColorFilter(applicationContext.resources.getColor(R.color.ligh_gray))
                            connection_hint.text = getText(R.string.not_connected)
                        }
                    }
                }
            }

        connection_refresh.setOnClickListener {
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.refresh_rotate)
            refresh.startAnimation(rotateAnimation)
            webSocket = client.newWebSocket(request, webSocketListener)
            sensorClient = client.newWebSocket(sensorRequest, webSocketListener)
        }


        fan_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    Log.d("Message", "${isChecked}")
                    val message = SensorMessage(
                        "IOT-GREENHOUSE/OPEN_FAN",
                        "ON"
                    )
                    webSocket!!.send(Gson().toJson(message))
                }
                else -> {
                    Log.d("Message", "${isChecked}")
                    val message = SensorMessage(
                        "IOT-GREENHOUSE/OPEN_FAN",
                        "OFF"
                    )
                    webSocket!!.send(Gson().toJson(message))
                }
            }
        }

        pump_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    Log.d("Message", "${isChecked}")
                    val message = SensorMessage(
                        "IOT-GREENHOUSE/OPEN_PUMP",
                        "ON"
                    )
                    webSocket!!.send(Gson().toJson(message))
                }
                else -> {
                    Log.d("Message", "${isChecked}")
                    val message = SensorMessage(
                        "IOT-GREENHOUSE/OPEN_PUMP",
                        "OFF"
                    )
                    webSocket!!.send(Gson().toJson(message))
                }
            }
        }

        sprinkler_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    Log.d("Message", "${isChecked}")
                    val message = SensorMessage(
                        "IOT-GREENHOUSE/OPEN_SPRINKLER",
                        "ON"
                    )
                    webSocket!!.send(Gson().toJson(message))
                }
                else -> {
                    Log.d("Message", "${isChecked}")
                    val message = SensorMessage(
                        "IOT-GREENHOUSE/OPEN_SPRINKLER",
                        "OFF"
                    )
                    webSocket!!.send(Gson().toJson(message))
                }
            }
        }

        connection_holder.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.refresh_rotate)
            refresh.startAnimation(animation)
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
