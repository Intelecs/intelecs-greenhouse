package com.example.iotgreenhouse.model

import com.squareup.moshi.Json

data class SendControls(
    @Json(name = "sensor")
    val sensor: String,

    @Json(name = "control")
    val control: String
)