package com.example.mall.Enum

enum class SortBy(private val sort: String) {
    HIGH_TO_LOW("High to Low"),
    LOW_TO_HIGH("Low to High"),
    NONE("None");

    fun sortMethod(): String {
        return sort
    }
}