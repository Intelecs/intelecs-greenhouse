package adapters.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iotgreenhouse.R
import model.NotifcationsModel
import kotlinx.android.synthetic.main.notification_item_list.view.*
import utils.inflate

class NotificationRecyclerAdapter (private val notifcations: MutableList<NotifcationsModel>) :
    RecyclerView.Adapter<NotificationRecyclerAdapter.ServiceHolder> () {

    override fun getItemCount(): Int {
        return notifcations.size
    }

    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        holder.bindData(notifcations[position])
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
        val btn_read = view.read_btn


            init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
        }

        fun bindData(notification: NotifcationsModel) {
            title.text = notification.title
            message.text = notification.message

            if(notification.isRead) {
                btn_read.visibility = View.GONE
            }
        }
    }
}