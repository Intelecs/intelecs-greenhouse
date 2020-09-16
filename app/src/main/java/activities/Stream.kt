package activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.beust.klaxon.Klaxon
import com.example.iotgreenhouse.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.norulab.exofullscreen.MediaPlayer
import com.norulab.exofullscreen.preparePlayer
import com.norulab.exofullscreen.setSource
import kotlinx.android.synthetic.main.activity_stream.*
import model.Stream
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class Stream : AppCompatActivity() {

    private val okHttpClient = OkHttpClient().newBuilder()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

        AndroidNetworking.initialize(applicationContext, okHttpClient)

        videoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

        MediaPlayer.exoPlayer?.addListener(object: Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    Player.STATE_BUFFERING -> {
                        loaderIndicator.visibility = View.VISIBLE
                    }
                    Player.STATE_ENDED -> {
                    }
                    Player.STATE_IDLE -> {
                        loaderIndicator.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        loaderIndicator.visibility = View.GONE
                    }
                }
            }
        })

        doAsync {
            AndroidNetworking.get("https://4jk6950pz3.execute-api.eu-west-1.amazonaws.com/greenHouse/get-stream-url")
                .build().getAsJSONObject(object: JSONObjectRequestListener {

                    override fun onResponse(response: JSONObject?) {
                        val parsedMessage = Klaxon().parse<Stream>(
                            """
                            $response
                        """.trimIndent()
                        )
                        val streamURL = parsedMessage!!.body
                        Log.d("Stream", streamURL)
                        runOnUiThread {
                            videoBrowser.webViewClient = WebViewClient()

                            videoBrowser.loadUrl(streamURL)
                            val webSettings = videoBrowser.settings
                            webSettings.javaScriptEnabled = true
                            initializePlayer(streamURL)


                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Stream Err", "$anError")
                    }
                })
        }
    }

    override fun onPause() {
        super.onPause()
        MediaPlayer.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        MediaPlayer.stopPlayer()
    }

    override fun onRestart() {
        super.onRestart()
        MediaPlayer.startPlayer()
    }

    override fun onResume() {
        super.onResume()
        MediaPlayer.startPlayer()
    }

    private fun initializePlayer(mediaUrl: String) {
        MediaPlayer.initialize(applicationContext)
        MediaPlayer.exoPlayer?.preparePlayer(videoPlayer, true)
        MediaPlayer.exoPlayer?.setSource(
            applicationContext,
            mediaUrl
        )
        MediaPlayer.startPlayer()

    }
}