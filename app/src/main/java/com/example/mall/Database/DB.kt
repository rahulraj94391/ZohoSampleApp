package com.example.mall

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mall.Database.*

const val DATABASE_NAME = "shopie.db"
private const val TAG = "Common_Tag_DB"

class DB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(UserTable.createTableStmt)
        db?.execSQL(AddressTable.createTableStmt)
        db?.execSQL(ProfileTable.createTableStmt)
        db?.execSQL(WishlistTable.createTableStmt)
        db?.execSQL(ProductCountPrice.createTableStmt)
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

    fun insertDummyData() {
        val db: SQLiteDatabase = this.writableDatabase

        val cv = ContentValues().apply {
            put(UserTable.COL_USERNAME, "rahul")
            put(UserTable.COL_PASSWORD, "12345678")
        }
        db.insert(UserTable.USER_TABLE_NAME, null, cv)

        val cv1 = ContentValues().apply {
            put(UserTable.COL_USERNAME, "vivek")
            put(UserTable.COL_PASSWORD, "87654321")
        }
        db.insert(UserTable.USER_TABLE_NAME, null, cv1)

        val cv2 = ContentValues().apply {
            put(AddressTable.COL_UID, "1")
            put(AddressTable.COL_FULL_NAME, "Rahul Raj")
            put(AddressTable.COL_MOBILE, "9155881234")
            put(AddressTable.COL_PIN_CODE, "826003")
            put(AddressTable.COL_ADDRESS, "Nutandih, PO Jagjiwan Nagar, Dhanbad, Jharkhand")
        }
        db.insert(AddressTable.ADDRESS_TABLE_NAME, null, cv2)

        val cv3 = ContentValues().apply {
            put(AddressTable.COL_UID, "1")
            put(AddressTable.COL_FULL_NAME, "Rahul Raj")
            put(AddressTable.COL_MOBILE, "9155881234")
            put(AddressTable.COL_PIN_CODE, "603210")
            put(AddressTable.COL_ADDRESS, "Sivram Homes, Vagai Nagar, Urapakkam, Tamil Nadu")
        }
        db.insert(AddressTable.ADDRESS_TABLE_NAME, null, cv3)

        val cv4 = ContentValues().apply {
            put(AddressTable.COL_UID, "2")
            put(AddressTable.COL_FULL_NAME, "Shaurya")
            put(AddressTable.COL_MOBILE, "7033667099")
            put(AddressTable.COL_PIN_CODE, "826001")
            put(AddressTable.COL_ADDRESS, "City Center, Dhanbad, Jharkhand")
        }
        db.insert(AddressTable.ADDRESS_TABLE_NAME, null, cv4)

        val cv5 = ContentValues().apply {
            put(AddressTable.COL_UID, "2")
            put(AddressTable.COL_FULL_NAME, "Sumit")
            put(AddressTable.COL_MOBILE, "7050662288")
            put(AddressTable.COL_PIN_CODE, "826003")
            put(AddressTable.COL_ADDRESS, "BCCL Colony, Dhanbad, Jharkhand")
        }
        db.insert(AddressTable.ADDRESS_TABLE_NAME, null, cv5)

        val cv6 = ContentValues().apply {
            put(ProfileTable.COL_UID, "1")
            put(ProfileTable.COL_FIRST_NAME, "Rahul")
            put(ProfileTable.COL_LAST_NAME, "Raj")
            put(ProfileTable.COL_MOBILE, "9155881234")
            put(ProfileTable.COL_EMAIL, "rahul.raj@zohocorp.com")
        }
        db.insert(ProfileTable.PROFILE_TABLE_NAME, null, cv6)

        val cv7 = ContentValues().apply {
            put(ProfileTable.COL_UID, "2")
            put(ProfileTable.COL_FIRST_NAME, "Vivek")
            put(ProfileTable.COL_LAST_NAME, "Dubey")
            put(ProfileTable.COL_MOBILE, "9876123451")
            put(ProfileTable.COL_EMAIL, "vivek.dubey@gmail.com")
        }
        db.insert(ProfileTable.PROFILE_TABLE_NAME, null, cv7)
    }

    fun isUser(userID: String, password: String): Boolean {
        val checkQuery = "SELECT * FROM ${UserTable.USER_TABLE_NAME} where ${UserTable.COL_USERNAME} = ? AND ${UserTable.COL_PASSWORD} = ?"
        val cursor: Cursor = readableDatabase.rawQuery(checkQuery, arrayOf(userID, password))
        val result: Boolean = cursor.count > 0
        cursor.close()
        return result
    }

    fun getUserId(username: String): Int {
        val checkQuery = "SELECT ${UserTable.COL_UID} FROM ${UserTable.USER_TABLE_NAME} where ${UserTable.COL_USERNAME} = ?"
        val cursor: Cursor = readableDatabase.rawQuery(checkQuery, arrayOf(username))
        cursor.moveToFirst()
        val userId = cursor.getInt(0)
        cursor.close()
        return userId
    }
}