package com.example.mall

fun String.rupeeString(price: Int) : String{
    return "₹ $price"
}

fun String.rupeeString(price: String) : String{
    return "₹ $price"
}

