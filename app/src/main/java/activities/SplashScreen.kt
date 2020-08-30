package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotgreenhouse.R
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Timer("Splash Screen", false).schedule(1500) {
            val home = Intent(
                this@SplashScreen,  MainActivity::class.java
            )
            startActivity(home)
            this@SplashScreen.finish()
        }
    }
}
