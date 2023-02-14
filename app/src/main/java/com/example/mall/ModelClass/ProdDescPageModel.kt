package com.example.mall.ModelClass

data class ProdDescPageModel(
    val imagesURL: MutableList<String>,
    val pid: Int,
    val name: String,
    val price: Int,
    val stock: Int,
    val specs: MutableMap<String, String>
)