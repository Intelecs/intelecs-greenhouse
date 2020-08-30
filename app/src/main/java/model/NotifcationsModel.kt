package model

data class NotifcationsModel(
    var sensor: String,
    var value: Float,
    var id: String,
    var isRead: Boolean
)