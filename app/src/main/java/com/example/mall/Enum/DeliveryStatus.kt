package com.example.mall.Enum

enum class DeliveryStatus(private val status: String) {
    NOT_YET_DISPATCHED("Not yet dispatched"),
    DISPATCHED("Dispatched"),
    IN_TRANSIT("In transit"),
    OUT_FOR_DELIVERY("Out for delivery"),
    REATTEMPT("Reattempt"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    RETURNED("Returned");

    fun status(): String {
        return status
    }
}