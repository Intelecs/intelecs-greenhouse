package com.example.iotgreenhouse.Network

import android.util.Log
import com.tinder.scarlet.Message
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.WebSocketEvent
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketNetwork : WebSocketListener() {
    var isConnected = false
    private val processor = PublishProcessor.create<WebSocketEvent>().toSerialized()
    fun observe(): Flowable<WebSocketEvent> = processor
    fun terminate() = processor.onComplete()
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
//        Log.d("WebSocket", "Error code $code, $reason")
        isConnected = false
        processor.onNext(WebSocketEvent.OnConnectionClosed(ShutdownReason(code, reason)))

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        webSocket.cancel()
        isConnected = false
//        Log.d("WebSocket", "Closing $code, $reason")
        processor.onNext(WebSocketEvent.OnConnectionClosing(
            ShutdownReason(code, reason)
        ))
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
//        Log.d("WebSocket", "Network Opened $response")
        isConnected = true
        processor.onNext(WebSocketEvent.OnConnectionOpened(
            webSocket, response
        ))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        isConnected = false
//        Log.d("WebSocket", "Message received $text")
        processor.onNext(WebSocketEvent.OnMessageReceived(
            Message.Text(text)
        ))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
//        Log.d("WebSocket", "Message received $bytes")
        processor.onNext(WebSocketEvent.OnMessageReceived(
            Message.Bytes(bytes.toByteArray())
        ))
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        processor.onNext(WebSocketEvent.OnConnectionFailed(t))
        isConnected = false
    }




}