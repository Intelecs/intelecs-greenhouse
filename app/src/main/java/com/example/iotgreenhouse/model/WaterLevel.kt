package com.example.iotgreenhouse.model

import com.squareup.moshi.Json

data class WaterLevel(
    @Json(name = "reading")
    val reading: String,

    @Json(name = "date_added")
    val date_added: String
)