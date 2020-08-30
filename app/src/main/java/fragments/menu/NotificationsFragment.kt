package fragments.menu

import adapters.recycler.NotificationRecyclerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.beust.klaxon.Klaxon
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.notifications_layout.*
import kotlinx.android.synthetic.main.notifications_layout.view.*
import model.NotifcationsModel
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.json.JSONArray

class NotificationsFragment : Fragment() {

    private val okHttpClient = OkHttpClient().newBuilder()
        .build()
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return  inflater.inflate(R.layout.notifications_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidNetworking.initialize(requireContext(), okHttpClient)
        linearLayoutManager = LinearLayoutManager(requireContext())
        val notificationsList = view.notifications
        notificationsList.layoutManager = linearLayoutManager

        doAsync {
            val BASE_URL = "https://4jk6950pz3.execute-api.eu-west-1.amazonaws.com/greenHouse/notification/get-all"
            requireActivity().runOnUiThread {
                loading.visibility = View.VISIBLE
                loading_details.visibility = View.VISIBLE
            }
            AndroidNetworking.get(BASE_URL).build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    val notifications: MutableList<NotifcationsModel> = ArrayList()
                    override fun onResponse(response: JSONArray?) {

                        if (response!!.length() > 10) {
                            for (i in 0 until 5) {
                                try {
                                    val notification = Klaxon().parse<NotifcationsModel>(
                                        """
                                    ${response.getJSONObject(i)}
                                """.trimIndent()
                                    )
                                    if (notification != null) {
                                        notifications.add(notification)
                                    }
                                } catch (e: Exception) {
                                    Log.d("Messages Exception", "${e.message}")
                                }
                            }
                        } else {
                            for (i in 0 until response!!.length()) {
                                try {
                                    val notification = Klaxon().parse<NotifcationsModel>(
                                        """
                                    ${response.getJSONObject(i)}
                                """.trimIndent()
                                    )
                                    if (notification != null) {
                                        notifications.add(notification)
                                    }
                                } catch (e: Exception) {
                                    Log.d("Messages Exception", "${e.message}")
                                }
                            }
                        }
                        requireActivity().runOnUiThread {
                            loading.visibility = View.GONE
                            loading_details.visibility = View.GONE
                            val adapter = NotificationRecyclerAdapter(notifications)
                            if(adapter.itemCount >= 1) {
                                notificationsList.adapter = adapter
                                view.not_found_holder.visibility = View.GONE
                            }
                            else {
                                view.not_found_holder.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Messages Error", "$anError")
                    }
                })
        }

    }
}