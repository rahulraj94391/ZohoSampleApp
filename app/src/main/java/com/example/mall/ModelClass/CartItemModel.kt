package com.example.mall.ModelClass

data class CartItemModel(
    val pid: Int,
    val productName: String,
    val price: Int,
    val quantity: Int,
    val thumbnailURL: String
)