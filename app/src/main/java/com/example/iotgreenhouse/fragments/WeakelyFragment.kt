package com.example.iotgreenhouse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.weakely_fragment.view.*


class WeakelyFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =  inflater.inflate(R.layout.weakely_fragment, container, false)

        val cartesian = AnyChart.column()
        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Monday", 24))
        data.add(ValueDataEntry("Tuesday", 20))
        data.add(ValueDataEntry("Wednesday", 24))
        data.add(ValueDataEntry("Thursday", 24))
        data.add(ValueDataEntry("Friday", 25))
        data.add(ValueDataEntry("Saturday", 19))
        data.add(ValueDataEntry("Sunday", 15))

        val column = cartesian.column(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("Weakly Readings of Temperature")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Days")
        cartesian.yAxis(0).title("Temperature (in Celsius)")
        view.weekly_chart.setProgressBar(view.progress_view)
        view.weekly_chart.setZoomEnabled(true)
        view.weekly_chart.setBackgroundColor(resources.getColor(R.color.gray))
        view.weekly_chart.setChart(cartesian)
        return view

    }
}