package com.example.mall.Enum

enum class Category(private val type: String) {
    ANDROID_PHONES("Android Phones"),
    IPHONES("iPhone"),
    TELEVISON("Television"),
    AIR_CONDITIONER("Air Conditioners"),
    HEADPHONES("Headphones"),
    REFRIGERATOR("Refrigerator"),
    EARPHONES("Earphone"),
    ALL_ITEMS("All Products");

    fun type(): String {
        return type
    }
}