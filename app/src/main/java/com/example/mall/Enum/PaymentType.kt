package com.example.mall.Enum

enum class PaymentType(private val paymentMethod: String) {
    UPI("UPI"),
    CARD("Card"),
    POD("Pay on Delivery"),
    EMI("EMI");

    fun paymentMethod(): String {
        return paymentMethod
    }
}