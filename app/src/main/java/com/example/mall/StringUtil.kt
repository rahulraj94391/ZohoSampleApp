package com.example.mall

fun rupeeString(price: Int): String {
    return "₹ $price"
}

fun rupeeString(price: String): String {
    return "₹ $price"
}