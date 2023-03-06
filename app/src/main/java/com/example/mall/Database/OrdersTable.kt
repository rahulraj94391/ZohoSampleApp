package com.example.mall.Database

object OrdersTable {
    const val ORDERS_TABLE_NAME = "orders"
    const val COL_UID = "uid"
    const val COL_PID = "pid"
    const val COL_QUANTITY = "quantity"
    const val COL_DELIVERY_STATUS = "delivery_status"
    const val COL_DELIVERY_DATE = "delivery_date"
    const val COL_ADDRESS_ID = "address_id"
    const val COL_ORDER_DATE = "order_date"
    const val COL_PAID_THROUGH = "paid_through"
    const val COL_OID = "oid"
    const val createTableStmt = "CREATE TABLE IF NOT EXISTS $ORDERS_TABLE_NAME ($COL_UID INTEGER, $COL_PID INTEGER, $COL_QUANTITY INTEGER, $COL_DELIVERY_STATUS BOOLEAN, $COL_DELIVERY_DATE INTEGER, $COL_ADDRESS_ID INTEGER, $COL_ORDER_DATE INTEGER, $COL_PAID_THROUGH TEXT, $COL_OID INTEGER, PRIMARY KEY($COL_OID AUTOINCREMENT))"
}