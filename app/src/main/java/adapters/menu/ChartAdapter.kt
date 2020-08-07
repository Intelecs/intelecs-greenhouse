package adapters.menu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fragments.analytics.MoistureFragment
import fragments.analytics.TemperatureFragment
import fragments.analytics.WaterLevelFragment

class ChartAdapter (private val fragment: FragmentManager, private var totalItems: Int)
    : FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TemperatureFragment()
            1 -> MoistureFragment()
            2 -> WaterLevelFragment()
            else -> TemperatureFragment()
        }
    }

    override fun getCount(): Int {
        return totalItems
    }
}