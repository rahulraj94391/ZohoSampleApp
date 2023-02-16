package com.example.mall.Database

object CartTable {
    const val CART_TABLE_NAME = "cart"
    const val COL_UID = "uid"
    const val COL_PID = "pid"
    const val COL_QUANTITY = "quantity"
    const val createTableStmt = "CREATE TABLE IF NOT EXISTS $CART_TABLE_NAME ($COL_UID INTEGER, $COL_PID INTEGER, $COL_QUANTITY INTEGER)"
}