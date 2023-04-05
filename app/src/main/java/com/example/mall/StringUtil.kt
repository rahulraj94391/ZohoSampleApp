package com.example.mall

import android.icu.text.DecimalFormat

private const val TAG = "CT_StringUtil"

fun rupeeString(price: Int): String {
    return "₹ ${DecimalFormat("##,##,##0").format(price)}"
}

fun rupeeString(price: String): String {
    return "₹ $price"
}


