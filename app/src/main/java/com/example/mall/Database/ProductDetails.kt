package com.example.mall.Database

object ProductDetails {
    private const val PRODUCT_DETAILS_TABLE_NAME = "prod_details"
    private const val COL_PID = "pid"           //INTEGER
    private const val COL_STOCK = "stock"       //INTEGER
    private const val COL_PRICE = "price"       //INTEGER
    private const val COL_NAME = "prod_name"    //TEXT
    const val createTableStmt: String = "CREATE TABLE IF NOT EXISTS $PRODUCT_DETAILS_TABLE_NAME ($COL_PID INTEGER, $COL_STOCK INTEGER, $COL_PRICE INTEGER, $COL_NAME TEXT)"
}