package com.example.mall.Database

object ProductDetails {
    const val PRODUCT_DETAILS_TABLE_NAME = "prod_details"
    const val COL_PID = "pid"
    const val COL_PROD_NAME = "prod_name"
    const val COL_PRICE = "price"
    const val COL_STOCK = "stock"
    const val COL_IMG0 = "imgURL0"
    const val COL_IMG1 = "imgURL1"
    const val COL_IMG2 = "imgURL2"
    const val createTableStmt: String = "CREATE TABLE IF NOT EXISTS $PRODUCT_DETAILS_TABLE_NAME ($COL_PID INTEGER, $COL_PROD_NAME TEXT, $COL_PRICE INTEGER, $COL_STOCK INTEGER, $COL_IMG0 TEXT, $COL_IMG1 TEXT, $COL_IMG2 TEXT, PRIMARY KEY($COL_PID AUTOINCREMENT))"
}