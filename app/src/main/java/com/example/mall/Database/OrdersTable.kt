package com.example.mall.Database

object OrdersTable {
    const val ORDERS_TABLE_NAME = "orders"
    const val COL_UID = "uid"
    const val COL_PID = "pid"
    const val COL_QUANTITY = "quantity"
    const val COL_IS_DELIVERED = "is_delivered"
    const val COL_DELIVERY_DATE = "delivery_date"
    const val createTableStmt = "CREATE TABLE IF NOT EXISTS $ORDERS_TABLE_NAME ($COL_UID INTEGER, $COL_PID INTEGER, $COL_QUANTITY INTEGER, $COL_IS_DELIVERED BOOLEAN, $COL_DELIVERY_DATE )"

}