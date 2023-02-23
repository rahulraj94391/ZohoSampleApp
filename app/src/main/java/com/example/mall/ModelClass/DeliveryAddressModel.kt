package com.example.mall.ModelClass

data class DeliveryAddressModel(
    val addressId: Int = -1,
    val fullName: String = "NOT_SET",
    val mobile: String = "NOT_SET",
    val pinCode: String = "NOT_SET",
    val address: String = "NOT_SET"
)