package com.example.mall.Database

object WishlistTable {
    const val WISHLIST_TABLE_NAME = "wishlists"
    const val COL_UID = "uid"
    const val COL_PID = "pid"
    const val createTableStmt = "CREATE TABLE IF NOT EXISTS $WISHLIST_TABLE_NAME ($COL_UID INTEGER, $COL_PID INTEGER)"
}