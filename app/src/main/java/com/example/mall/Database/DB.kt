package com.example.mall

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mall.Database.*

const val DATABASE_NAME = "shopie.db"
private const val TAG = "Common_Tag_DB"

class DB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(AddressTable.createTableStmt)
        db?.execSQL(CartTable.createTableStmt)
        db?.execSQL(CategoryTagTable.createTableStmt)
        db?.execSQL(OrdersTable.createTableStmt)
        db?.execSQL(ProdSpecsTable.createTableStmt)
        db?.execSQL(ProductDetails.createTableStmt)
        db?.execSQL(ProfileTable.createTableStmt)
        db?.execSQL(UserTable.createTableStmt)
        db?.execSQL(WishlistTable.createTableStmt)
        // 9 tables
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun queryProfileData(uid: Int): Array<String> {
        lateinit var profileData: Array<String>
        val query = "SELECT * FROM ${ProfileTable.PROFILE_TABLE_NAME} WHERE ${ProfileTable.COL_UID} = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            val fullName = cursor.getString(1) + " " + cursor.getString(2)
            val mob = cursor.getString(3)
            val email = cursor.getString(4)
            profileData = arrayOf(fullName, mob, email)
        } else {
            profileData = arrayOf("Update Details", "Ph. No.", "Email")
        }
        cursor.close()
        return profileData
    }

    fun isUser(userID: String, password: String): Boolean {
        val checkQuery = "SELECT * FROM ${UserTable.USER_TABLE_NAME} where ${UserTable.COL_USERNAME} = ? AND ${UserTable.COL_PASSWORD} = ?"
        val cursor: Cursor = readableDatabase.rawQuery(checkQuery, arrayOf(userID, password))
        val result: Boolean = cursor.count > 0
        cursor.close()
        return result
    }

    /** returns UID from username */
    fun getUserId(username: String): Int {
        val checkQuery = "SELECT ${UserTable.COL_UID} FROM ${UserTable.USER_TABLE_NAME} where ${UserTable.COL_USERNAME} = ?"
        val cursor: Cursor = readableDatabase.rawQuery(checkQuery, arrayOf(username))
        cursor.moveToFirst()
        val userId = cursor.getInt(0)
        cursor.close()
        return userId
    }
}