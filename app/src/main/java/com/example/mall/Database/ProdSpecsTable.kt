package com.example.mall.Database

object ProdSpecsTable {
    const val PRODUCT_SPECS_TABLE = "prod_specs"
    const val COL_PID = "pid" // INTEGER
    const val COL_KEY = "key" // TEXT
    const val COL_VAL = "val" // TEXT
    const val createTableStmt: String = "CREATE TABLE IF NOT EXISTS $PRODUCT_SPECS_TABLE ($COL_PID INTEGER, $COL_KEY TEXT, $COL_VAL TEXT)"
}