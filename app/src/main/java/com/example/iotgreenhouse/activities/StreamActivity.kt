package com.example.iotgreenhouse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.example.iotgreenhouse.R
import com.takusemba.rtmppublisher.Publisher
import com.takusemba.rtmppublisher.PublisherListener
import kotlinx.android.synthetic.main.activity_stream.*

class StreamActivity : AppCompatActivity(), PublisherListener {

    private lateinit var publisher: Publisher
    private val streamingURL = "rtmp:flash.oit.duke.edu/vod/_definst_"

    private var isCounting = false
    private var thread: Thread? = null
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)


        if (streamingURL.isBlank()) {
            Toast.makeText(this, R.string.url_error, Toast.LENGTH_SHORT)
                .apply { setGravity(Gravity.CENTER, 0, 0) }
                .run { show() }
        } else {
            publisher = Publisher.Builder(this)
                .setGlView(stream_view)
                .setUrl(streamingURL)
                .setSize(Publisher.Builder.DEFAULT_WIDTH, Publisher.Builder.DEFAULT_HEIGHT)
                .setAudioBitrate(Publisher.Builder.DEFAULT_AUDIO_BITRATE)
                .setVideoBitrate(Publisher.Builder.DEFAULT_VIDEO_BITRATE)
                .setCameraMode(Publisher.Builder.DEFAULT_MODE)
                .setListener(this)
                .build()
        }
    }

    override fun onDisconnected() {
        Toast.makeText(this, R.string.disconnected_publishing, Toast.LENGTH_SHORT)
            .apply { setGravity(Gravity.CENTER, 0, 0) }
            .run { show() }

        stopCounting()
    }

    override fun onFailedToConnect() {
        Toast.makeText(this, R.string.failed_publishing, Toast.LENGTH_SHORT)
            .apply { setGravity(Gravity.CENTER, 0, 0) }
            .run { show() }

        stopCounting()

    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
    override fun onStarted() {
        Toast.makeText(this, R.string.started_publishing, Toast.LENGTH_SHORT)
            .apply { setGravity(Gravity.CENTER, 0, 0) }
            .run { show() }

        startCounting()
    }

    override fun onStopped() {
        Toast.makeText(this, R.string.stopped_publishing, Toast.LENGTH_SHORT)
            .apply { setGravity(Gravity.CENTER, 0, 0) }
            .run { show() }
        stopCounting()
    }



    private fun startCounting() {
        isCounting = true
        live_label.text = getString(R.string.publishing_label, 0L.format(), 0L.format())
        live_label.visibility = View.VISIBLE

    }

    private fun stopCounting() {
        live_label.text = ""
        live_label.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        stopCounting()
        finish()
    }

    override fun onResume() {
        super.onResume()
        stopCounting()
    }

    private fun Long.format(): String {
        return String.format("%02d", this)
    }
}
