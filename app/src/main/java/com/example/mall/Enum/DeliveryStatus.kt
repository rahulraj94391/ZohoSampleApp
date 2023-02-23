package com.example.mall.Enum

enum class DeliveryStatus(private val status: String) {
    IN_TRANSIT("In transit"),
    DELIVERED("Delivered");

    fun status(): String {
        return status
    }
}