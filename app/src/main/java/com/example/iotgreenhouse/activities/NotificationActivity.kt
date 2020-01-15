package com.example.iotgreenhouse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iotgreenhouse.R
import com.example.iotgreenhouse.adapters.NotificationRecyclerAdapter
import com.example.iotgreenhouse.model.NotifcationsModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val linearLayoutManager = LinearLayoutManager(applicationContext)
        reycler_notifications.layoutManager = linearLayoutManager
        addNotifcations()

    }

    fun addNotifcations() {
        val notifications: MutableList<NotifcationsModel> = ArrayList()

        notifications.add(
            NotifcationsModel("Temperature Alert",
            "Temperature has reached 30 celsius more than 27 of the threshold", false)
        )

        notifications.add(NotifcationsModel("Moisture Alert",
            "Moisture has reached 70 percent more than 30 of the threshold", true))

        val notificationsAdapter = NotificationRecyclerAdapter(notifications,this)
        reycler_notifications.adapter = notificationsAdapter

        if (notificationsAdapter.itemCount == 0) {
            not_found_holder.visibility = View.VISIBLE
            loading.visibility = View.GONE
            loading_details.visibility = View.GONE
        }
        else {
            not_found_holder.visibility = View.GONE
            loading.visibility = View.GONE
            loading_details.visibility = View.GONE
        }
    }
}
