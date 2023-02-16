package com.example.mall.ModelClass

data class ProductListModel(
    var pid:Int,
    var prodName: String,
    var imgURL: String,
    var prodPrice: Int,
    var stock: Int
)