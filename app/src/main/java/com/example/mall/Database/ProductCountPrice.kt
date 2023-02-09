package com.example.mall.Database



object ProductCountPrice {
    const val PRODUCT_COUNT_PRICE_TABLE_NAME = "product_count_price"
    const val COL_PID = "pid"           //INTEGER
    const val COL_COUNT = "count"       //INTEGER
    const val COL_PRICE = "price"       //INTEGER
    const val createTableStmt = "CREATE TABLE IF NOT EXISTS $PRODUCT_COUNT_PRICE_TABLE_NAME ($COL_PID INTEGER, $COL_COUNT INTEGER, $COL_PRICE INTEGER)"
}