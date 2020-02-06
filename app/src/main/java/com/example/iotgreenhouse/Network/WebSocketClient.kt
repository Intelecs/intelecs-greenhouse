package com.example.iotgreenhouse.Network

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object WebSocketClient  {
    const val SOCKET_BASE_URL = "ws://192.168.43.66:8000/ws/sensors"
    val backoffStrategy = ExponentialWithJitterBackoffStrategy(5000, 5000)

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val websocket = OkHttpWebSocket(okHttpClient,
        OkHttpWebSocket.SimpleRequestFactory(
            {Request.Builder().url(SOCKET_BASE_URL).build()},
            { ShutdownReason.GRACEFUL}
        )
    )

    val configuration = Scarlet.Configuration(
        messageAdapterFactories = listOf(MoshiMessageAdapter.Factory()),
        streamAdapterFactories = listOf(RxJava2StreamAdapterFactory())
    )

    val scarlet = Scarlet(websocket, configuration)
    val iotService = scarlet.create<SocketService>()


}