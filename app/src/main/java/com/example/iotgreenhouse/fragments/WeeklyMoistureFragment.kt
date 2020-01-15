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
import kotlinx.android.synthetic.main.chart_fragement.view.*

class WeeklyMoistureFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.chart_fragement, container, false)

        val cartesian = AnyChart.column()


        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Mon", 24))
        data.add(ValueDataEntry("Tue", 20))
        data.add(ValueDataEntry("Wed", 24))
        data.add(ValueDataEntry("Thur", 24))
        data.add(ValueDataEntry("Fri", 25))
        data.add(ValueDataEntry("Sat", 19))
        data.add(ValueDataEntry("Sun", 15))
        val column = cartesian.column(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}{groupsSeparator: } %")

        cartesian.animation(true)
        cartesian.title("Weakly Readings of Moisture")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("% {%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Days")
        cartesian.yAxis(0).title("Moisture (in %)")
        cartesian.background("#3E4043")

        view.chart.setProgressBar(view.progress)
        column.fill("#FF3C53")
        column.stroke("#FF3C53")
        view.chart.setBackgroundColor("#3E4043")
        view.chart.setZoomEnabled(false)
        view.chart.setChart(cartesian)

        return view

    }
}