package com.example.mall.Enum

enum class DeliveryStatus(private val status: String) {
    CANCELED("Canceled"),
    REATTEMPT("Reattempt"),
    NOT_DELIVERED("Not Delivered"),
    DISPATCHED("Dispatched"),
    IN_TRANSIT("In transit"),
    OUT_FOR_DELIVERY("Out for delivery"),
    DELIVERED("Delivered");

    fun status(): String {
        return status
    }
}