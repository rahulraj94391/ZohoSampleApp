package com.example.mall.ModelClass

data class PriceDetailsModel(
    val totalItemCount: Int,
    val priceBeforeDiscount: Int,
    val discount: Int,
    val totalPriceToPay: Int
)