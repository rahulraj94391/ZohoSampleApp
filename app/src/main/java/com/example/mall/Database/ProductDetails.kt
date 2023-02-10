package com.example.mall.Database

object ProductDetails {
    const val PRODUCT_DETAILS_TABLE_NAME = "prod_details"
    const val COL_PID = "pid"           //INTEGER
    const val COL_STOCK = "stock"       //INTEGER
    const val COL_PRICE = "price"       //INTEGER
    const val COL_NAME = "prod_name"    //TEXT
    const val COL_DESC = "prod_desc"    //TEXT
    const val createTableStmt: String = "CREATE TABLE IF NOT EXISTS $PRODUCT_DETAILS_TABLE_NAME ($COL_PID INTEGER, $COL_STOCK INTEGER, $COL_PRICE INTEGER, $COL_NAME TEXT, $COL_DESC TEXT)"
}