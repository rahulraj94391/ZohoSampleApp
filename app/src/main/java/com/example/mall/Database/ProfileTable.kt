package com.example.mall.Database

object ProfileTable {
    const val PROFILE_TABLE_NAME = "profiles"
    const val COL_UID = "uid"           //INTEGER
    const val COL_FIRST_NAME = "fName"  //TEXT
    const val COL_LAST_NAME = "lName"   //TEXT
    const val COL_MOBILE = "mob"        //TEXT
    const val COL_EMAIL = "email"       //TEXT
    const val createTableStmt = "CREATE TABLE IF NOT EXISTS $PROFILE_TABLE_NAME ($COL_UID INTEGER, $COL_FIRST_NAME TEXT, $COL_LAST_NAME TEXT, $COL_MOBILE TEXT, $COL_EMAIL TEXT)"
}