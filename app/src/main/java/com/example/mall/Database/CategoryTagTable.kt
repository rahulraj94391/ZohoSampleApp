package com.example.mall.Database

//ASSETS
object CategoryTagTable {
    const val CATEGORY_TAG_TABLE_NAME = "category_tags"
    const val COL_PID = "pid"                       // INTEGER
    const val COL_CATEGORY_TAG = "category_tag"     // TEXT
    const val createTableStmt: String = "CREATE TABLE IF NOT EXISTS $CATEGORY_TAG_TABLE_NAME ($COL_PID INTEGER, $COL_CATEGORY_TAG TEXT)"
}