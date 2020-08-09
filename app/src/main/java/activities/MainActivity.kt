package activities

import adapters.menu.MenuAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.iotgreenhouse.R
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private val okHttpClient = OkHttpClient().newBuilder()
        .build()
    private var BASE_URL = "https://4jk6950pz3.execute-api.eu-west-1.amazonaws.com/greenHouse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener{
            doAsync {
                Log.d("Firebase Messaging", "A notification Message has been created ${it.token}")
                AndroidNetworking.post("${BASE_URL}/register-token?deviceToken=${it.token}").setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            Log.d("TOKEN", "$response")
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("TOKEN", "$anError")
                        }
                    })
            }
        }

        AndroidNetworking.initialize(applicationContext, okHttpClient)

        bottomBar.add(MeowBottomNavigation.Model(0, R.drawable.ic_home))
        bottomBar.add(MeowBottomNavigation.Model(1, R.drawable.ic_bell))
        bottomBar.add(MeowBottomNavigation.Model(2, R.drawable.ic_hardware))
        bottomBar.setCount(1, "0")
        bottomBar.show(0)

        val menuAdapter = MenuAdapter(
            supportFragmentManager, 3
        )

        menuViewPager.adapter = menuAdapter
        bottomBar.setOnShowListener {
            when(it.id) {
                0 -> menuViewPager.currentItem = it.id
                1 -> menuViewPager.currentItem = it.id
                2 -> menuViewPager.currentItem = it.id
                else -> menuViewPager.currentItem = 0
            }
        }

        menuViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageSelected(position: Int) {
                bottomBar.show(position)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }
}
