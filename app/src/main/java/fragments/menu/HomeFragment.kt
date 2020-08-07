package fragments.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.amazonaws.regions.Regions
import com.amazonaws.services.iot.AWSIot
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.beust.klaxon.Klaxon
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.home_layout.*
import kotlinx.android.synthetic.main.home_layout.view.*
import kotlinx.android.synthetic.main.main_toolbar.*
import model.Payload
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import java.util.*

class HomeFragment : Fragment() {

    private val ENDPOINT = "a2wp7mzzv5nytk-ats.iot.eu-west-1.amazonaws.com"
    private var BASE_URL = "https://4jk6950pz3.execute-api.eu-west-1.amazonaws.com/greenHouse"
    private lateinit var mqttManager: AWSIotMqttManager
    private val clientID = UUID.randomUUID().toString()
    private val topics = arrayOf(
        "telemetries/moisture",
        "telemetries/temperature",
        "telemetries/humidity",
        "telemetries/waterLevel"
    )

    private val okHttpClient = OkHttpClient().newBuilder()
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val credentialProvider = CognitoCachingCredentialsProvider(
            requireContext(),
            "eu-west-1:52330565-0fd3-4750-b5d4-8463066df64f",
            Regions.EU_WEST_1
        )

        AndroidNetworking.initialize(requireContext(), okHttpClient)
        mqttManager = AWSIotMqttManager(clientID, ENDPOINT)
        connectMqtt(credentialProvider)
        checkState()

        fan_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            doAsync {
                if (isChecked) {
                    AndroidNetworking.post("${BASE_URL}/fan/act?state=1").setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                Log.d("Shadow", "$response")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("Shadow", "$anError")
                            }
                        })
                } else {
                    AndroidNetworking.post("${BASE_URL}/fan/act?state=0").setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                Log.d("Shadow", "$response")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("Shadow", "$anError")
                            }
                        })
                }
            }
        }

        sprinkler_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            doAsync {
                if (isChecked) {
                    AndroidNetworking.post("${BASE_URL}/sprinkler/act?state=1")
                        .setPriority(Priority.HIGH).build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                Log.d("Shadow", "$response")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("Shadow", "$anError")
                            }
                        })
                } else {
                    AndroidNetworking.post("${BASE_URL}/sprinkler/act?state=0")
                        .setPriority(Priority.HIGH).build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                Log.d("Shadow", "$response")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("Shadow", "$anError")
                            }
                        })
                }
            }
        }

        pump_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            doAsync {
                if (isChecked) {
                    AndroidNetworking.post("${BASE_URL}/tank/act?state=1")
                        .setPriority(Priority.HIGH).build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                Log.d("Shadow", "$response")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("Shadow", "$anError")
                            }
                        })
                } else {
                    AndroidNetworking.post("${BASE_URL}/tank/act?state=0")
                        .setPriority(Priority.HIGH).build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                Log.d("Shadow", "$response")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("Shadow", "$anError")
                            }
                        })
                }
            }
        }

    }

    private fun checkState() {
        doAsync {
            AndroidNetworking.get("${BASE_URL}/fan/state").build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        val result = Integer.parseInt(response!!.replace("\"", ""))
                        Log.d("Shadow", "$result")
                        if (result == 1) {
                            requireActivity().runOnUiThread {
                                fan_switch.isChecked = true
                            }
                        }
                        else {
                            requireActivity().runOnUiThread {
                                fan_switch.isChecked = false
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Shadow", "$anError")
                    }
                })

            AndroidNetworking.get("${BASE_URL}/tank/state").build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        val result = Integer.parseInt(response!!.replace("\"", ""))
                        Log.d("Shadow", "$result")
                        if (result == 1) {
                            requireActivity().runOnUiThread {
                                pump_switch.isChecked = true
                            }
                        }
                        else {
                            requireActivity().runOnUiThread {
                                pump_switch.isChecked = false
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Shadow", "$anError")
                    }
                })

            AndroidNetworking.get("${BASE_URL}/sprinkler/state").build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        val result = Integer.parseInt(response!!.replace("\"", ""))
                        Log.d("Shadow", "$result")
                        if (result == 1) {
                            requireActivity().runOnUiThread {
                                sprinkler_switch.isChecked = true
                            }
                        }
                        else {
                            requireActivity().runOnUiThread {
                                sprinkler_switch.isChecked = false
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Shadow", "$anError")
                    }
                })
        }
    }

    private fun connectMqtt(credentialsProvider: CognitoCachingCredentialsProvider) {
        try {

            mqttManager.connect(credentialsProvider, object : AWSIotMqttClientStatusCallback {
                @SuppressLint("SetTextI18n")
                override fun onStatusChanged(
                    status: AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus?,
                    throwable: Throwable?
                ) {
                    Log.d("IoT Core", "${status}, ${throwable}")
                    requireActivity().runOnUiThread {
                        when (status) {
                            AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected -> {
                                connection_image.setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.red
                                    )
                                )
                                connection_hint.text = "Connected"
                                refresh.clearAnimation()
                                connection_refresh.visibility = View.GONE
                                doAsync {
                                    try {
                                        for (topic in topics) {
                                            mqttManager.publishString(
                                                "OPEN",
                                                "\$aws/things/GreenHouseMeter/shadow/name/FAN",
                                                AWSIotMqttQos.QOS0
                                            )
                                            mqttManager.subscribeToTopic(
                                                topic,
                                                AWSIotMqttQos.QOS0,
                                                object : AWSIotMqttNewMessageCallback {
                                                    override fun onMessageArrived(
                                                        topic: String?,
                                                        data: ByteArray?
                                                    ) {
                                                        try {
                                                            val message =
                                                                String(data!!, Charsets.UTF_8)
                                                            val parsedMessage =
                                                                Klaxon().parse<Payload>(
                                                                    """
                                                                $message
                                                            """.trimIndent()
                                                                )
                                                            if (parsedMessage != null) {
                                                                if (parsedMessage.sensor == "temperature") {
                                                                    requireActivity().runOnUiThread {
                                                                        temp_reading.text =
                                                                            parsedMessage.value.toString()
                                                                    }
                                                                }
                                                                if (parsedMessage.sensor == "level") {
                                                                    requireActivity().runOnUiThread {
                                                                        level_reading.text =
                                                                            parsedMessage.value.toString()
                                                                        progressView.progress =
                                                                            parsedMessage.value
                                                                    }
                                                                }


                                                                if (parsedMessage.sensor == "moisture") {
                                                                    requireActivity().runOnUiThread {
                                                                        moist_reading.text =
                                                                            parsedMessage.value.toString()
                                                                    }
                                                                }
                                                            }
                                                            Log.d(
                                                                "IoT Core",
                                                                "$topic, $parsedMessage"
                                                            )
                                                        } catch (e: Exception) {
                                                            Log.d("IoT Core", "$e")
                                                        }
                                                    }
                                                })
                                        }
                                    } catch (e: Exception) {
                                        Log.d("IoT Core", "${e}")
                                    }
                                }
                            }
                            AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Reconnecting -> {
                                connection_refresh.visibility = View.VISIBLE

                                refresh_hint.text = "Re connecting"
                                val refreshAnimation = AnimationUtils.loadAnimation(
                                    requireContext(),
                                    R.anim.refresh_rotate
                                )
                                refresh.startAnimation(refreshAnimation)
                                connection_image.setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.ligh_gray
                                    )
                                )
                                connection_hint.text = "Not Connected"

                            }
                            AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting -> {
                                connection_image.setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.ligh_gray
                                    )
                                )
                                connection_hint.text = "Not Connected"
                                connection_refresh.visibility = View.VISIBLE
                                refresh_hint.text = "Connecting"

                                val refreshAnimation = AnimationUtils.loadAnimation(
                                    requireContext(),
                                    R.anim.refresh_rotate
                                )
                                refresh.startAnimation(refreshAnimation)
                            }
                            AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost -> {
                                connection_image.setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.ligh_gray
                                    )
                                )
                                connection_hint.text = "Connection Lost"
                            }
                        }
                    }

                }
            })
        } catch (e: Exception) {
        }
    }
}