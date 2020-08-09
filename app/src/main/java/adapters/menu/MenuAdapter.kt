package adapters.menu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fragments.menu.HardwareFragment
import fragments.menu.HomeFragment
import fragments.menu.NotificationsFragment


class MenuAdapter(
    fragment: FragmentManager,
    private var totalItems: Int
) : FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> NotificationsFragment()
            2 -> HardwareFragment()
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return totalItems
    }
}