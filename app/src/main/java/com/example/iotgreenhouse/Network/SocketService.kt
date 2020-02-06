package com.example.iotgreenhouse.Network

import com.example.iotgreenhouse.model.SendControls
import com.example.iotgreenhouse.model.Temperature
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface SocketService  {

    @Receive
    fun observeNetwork() : Flowable<WebSocketEvent>

    @Send
    fun sendControl(action: SendControls)

    @Receive
    fun obeserveTemperature(): Flowable<Temperature>
}