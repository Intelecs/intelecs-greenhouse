package activities

import adapters.menu.MenuAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar.add(MeowBottomNavigation.Model(0, R.drawable.ic_home))
        bottomBar.add(MeowBottomNavigation.Model(1, R.drawable.ic_analytics))
        bottomBar.add(MeowBottomNavigation.Model(2, R.drawable.ic_bell))
        bottomBar.add(MeowBottomNavigation.Model(3, R.drawable.ic_hardware))
        bottomBar.setCount(2, "0")
        bottomBar.show(0)

        val menuAdapter = MenuAdapter(
            supportFragmentManager, 4
        )

        menuViewPager.adapter = menuAdapter
        bottomBar.setOnShowListener {
            when(it.id) {
                0 -> menuViewPager.currentItem = it.id
                1 -> menuViewPager.currentItem = it.id
                2 -> menuViewPager.currentItem = it.id
                3 -> menuViewPager.currentItem = it.id
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
