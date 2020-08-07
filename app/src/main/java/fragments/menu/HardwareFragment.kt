package fragments.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.home_layout.view.*
import kotlinx.android.synthetic.main.threshold_layout.view.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.json.JSONObject


class HardwareFragment : Fragment() {

    private val okHttpClient = OkHttpClient().newBuilder()
        .build()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return  inflater.inflate(R.layout.threshold_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        AndroidNetworking.initialize(requireContext(), okHttpClient)

        view.save_btn.setOnClickListener {
            val moisture = view.moist_input.text.toString().toIntOrNull()
            val temperature = view.temp_input.text.toString().toIntOrNull()
            val level = view.tank_input.text.toString().toIntOrNull()

            Log.d("Params", "${moisture}")
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Sending Data", Toast.LENGTH_SHORT).show()
            }

            if (moisture != null && temperature != null && level != null) {
               doAsync {
                   var BASE_URL = "https://4jk6950pz3.execute-api.eu-west-1.amazonaws.com/greenHouse"
                   BASE_URL = "${BASE_URL}//params/act?moisture=${moisture}&temperature=${temperature}&level=${level}"
                   AndroidNetworking.post(BASE_URL)
                       .setPriority(Priority.HIGH).build()
                       .getAsJSONObject(object : JSONObjectRequestListener {
                           override fun onResponse(response: JSONObject) {
                               Log.d("Shadow", "$response")
                               requireActivity().runOnUiThread {
                                   Toast.makeText(requireContext(), "Parameters Updated", Toast.LENGTH_SHORT).show()
                               }
                           }

                           override fun onError(anError: ANError?) {
                               Log.d("Shadow", "$anError")
                               Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                           }
                       })
               }
            }
        }
    }
}