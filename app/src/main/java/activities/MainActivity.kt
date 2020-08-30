package activities

import adapters.menu.MenuAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.iotgreenhouse.R
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.json.JSONArray
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

        bottomBar.show(0)

        doAsync {
            val BASE_URL = "https://4jk6950pz3.execute-api.eu-west-1.amazonaws.com/greenHouse/notification/get-all"
            AndroidNetworking.get(BASE_URL).build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {
                        Log.d("Messages Lit", "${response!!.length()}")
                        if(response.length() > 0) {
                            runOnUiThread {
                                bottomBar.setCount(1, response.length().toString())
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Shadow", "$anError")
                    }
                })
        }

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
