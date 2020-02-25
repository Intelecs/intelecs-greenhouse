package com.example.iotgreenhouse.model

import com.squareup.moshi.Json

data class SensorMessage(
    val topic: String,
    val payload: String
)