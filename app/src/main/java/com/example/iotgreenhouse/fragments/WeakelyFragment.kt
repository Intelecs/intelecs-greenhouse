package com.example.iotgreenhouse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.iotgreenhouse.R
import kotlinx.android.synthetic.main.weakely_fragment.*


class WeakelyFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.weakely_fragment, container, false)

        val weeklyTempratureFagement = WeeklyTempratureFagement()
        val tempratureFagement = fragmentManager!!.beginTransaction()
        tempratureFagement.add(R.id.temp_frame, weeklyTempratureFagement)
        tempratureFagement.commit()

        val weeklyMoistureFragment = WeeklyMoistureFragment()
        val moistureFragment = fragmentManager!!.beginTransaction()
        moistureFragment.add(R.id.moist_frame, weeklyMoistureFragment)
        moistureFragment.commit()

        return view

    }
}