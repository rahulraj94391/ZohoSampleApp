package com.example.mall.ModelClass

data class CartItemModel(
    val productId: Int,
    val productName: String,
    val price: Int,
    val quantity: Int,
    val thumbnailURL: String
)