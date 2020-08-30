package adapters.recycler

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.example.iotgreenhouse.R
import model.NotifcationsModel
import kotlinx.android.synthetic.main.notification_item_list.view.*
import okhttp3.OkHttpClient
import utils.inflate

class NotificationRecyclerAdapter (private val notifications: MutableList<NotifcationsModel>) :
    RecyclerView.Adapter<NotificationRecyclerAdapter.ServiceHolder> () {

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        holder.bindData(notifications[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceHolder {
        val infaledView = parent.inflate(R.layout.notification_item_list, false)
        return ServiceHolder(
            infaledView
        )
    }

    class ServiceHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val title = view.noty_title
        val message = view.noty_msg
        var id: String = ""


            init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
        }

        fun bindData(notification: NotifcationsModel) {
           val okHttpClient = OkHttpClient().newBuilder()
                .build()

            title.text = "${notification.sensor} Sensor"
            message.text = "Out of threshold value of ${notification.value} reached."
            id = notification.id
        }
    }
}