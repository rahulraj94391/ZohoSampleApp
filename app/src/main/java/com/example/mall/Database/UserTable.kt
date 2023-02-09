package com.example.mall.Database

object UserTable {
    const val USER_TABLE_NAME = "users"
    const val COL_UID = "uid"
    const val COL_USERNAME = "username"
    const val COL_PASSWORD = "password"
    const val createTableStmt: String = "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME ($COL_UID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_USERNAME TEXT, $COL_PASSWORD TEXT)"
}