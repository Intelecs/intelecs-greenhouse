package com.example.iotgreenhouse.adapters

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.iotgreenhouse.R
import com.example.iotgreenhouse.model.NotifcationsModel
import com.example.iotgreenhouse.utils.inflate
import kotlinx.android.synthetic.main.notification_item_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationRecyclerAdapter (private val notifcations: MutableList<NotifcationsModel>, private val context: Context?) :
    RecyclerView.Adapter<NotificationRecyclerAdapter.ServiceHolder> () {

    val _context = context

    override fun getItemCount(): Int {
        return notifcations.size
    }

    override fun onBindViewHolder(holder: NotificationRecyclerAdapter.ServiceHolder, position: Int) {
        holder.bindData(notifcations.get(position), context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRecyclerAdapter.ServiceHolder {
        val infaledView = parent.inflate(R.layout.notification_item_list, false)
        return ServiceHolder(infaledView)
    }

    class ServiceHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val title = view.noty_title
        val message = view.noty_msg
        val btn_read = view.read_btn


            init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
        }

        fun bindData(notification: NotifcationsModel, context: Context?) {
            title.text = notification.title
            message.text = notification.message

            if(notification.isRead) {
                btn_read.visibility = View.GONE
            }
        }
    }
}