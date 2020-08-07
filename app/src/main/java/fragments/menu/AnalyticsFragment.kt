package fragments.menu

import adapters.menu.ChartAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.iotgreenhouse.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.analytics_layout.*
import kotlinx.android.synthetic.main.analytics_layout.view.*

class AnalyticsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return  inflater.inflate(R.layout.analytics_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.tabs_holder.tabGravity = TabLayout.GRAVITY_FILL
        view.tabs_holder.addTab(
            view.tabs_holder.newTab().setText(R.string.temperature)
        )
        view.tabs_holder.addTab(
            view.tabs_holder.newTab().setText(R.string.moisture)
        )

        view.tabs_holder.addTab(
            view.tabs_holder.newTab().setText(R.string.water_level)
        )

        val chartAdapter = ChartAdapter(
            requireActivity().supportFragmentManager, view.tabs_holder.tabCount
        )

        chartViewerPager.adapter = chartAdapter

        view.tabs_holder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                chartViewerPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

    }
}