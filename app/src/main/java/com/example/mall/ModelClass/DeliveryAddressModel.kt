package com.example.mall.ModelClass

data class DeliveryAddressModel(
    val addressId: Int,
    val fullName: String,
    val mobile: String,
    val pinCode: String,
    val address: String
)