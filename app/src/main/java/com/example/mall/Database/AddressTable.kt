package com.example.mall.Database

object AddressTable {
        const val ADDRESS_TABLE_NAME = "addresses"
        const val COL_UID = "uid"
        const val COL_FULL_NAME = "full_name"
        const val COL_MOBILE = "mobile"
        const val COL_PIN_CODE = "pin_code"
        const val COL_ADDRESS = "address"
        const val createTableStmt = "CREATE TABLE IF NOT EXISTS $ADDRESS_TABLE_NAME ($COL_UID INTEGER, $COL_FULL_NAME TEXT, $COL_MOBILE TEXT, $COL_PIN_CODE TEXT, $COL_ADDRESS TEXT)"
}