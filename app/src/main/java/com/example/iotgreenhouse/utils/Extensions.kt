package com.example.iotgreenhouse.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attactToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attactToRoot)
}