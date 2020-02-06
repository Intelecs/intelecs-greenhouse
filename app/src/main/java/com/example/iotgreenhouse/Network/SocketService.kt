package com.example.iotgreenhouse.Network

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send

interface SocketService  {
    @Send
    fun openVentilation()
}