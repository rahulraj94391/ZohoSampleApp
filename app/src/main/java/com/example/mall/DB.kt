package com.example.mall

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME = "shopie.db"
private const val TAG = "Common_Tag_DB"

class DB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(UserTable.createUsersTableStmt)
        db?.execSQL(AddressTable.createAddressTableStmt)
        db?.execSQL(ProfileTable.createProfileTableStmt)
        db?.execSQL(WishlistTable.createWishlistTableStmt)
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

class UserTable {
    companion object {
        const val USER_TABLE_NAME = "users"
        const val COL_UID = "uid"
        const val COL_USERNAME = "username"
        const val COL_PASSWORD = "password"
        const val createUsersTableStmt: String = "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME ($COL_UID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_USERNAME TEXT, $COL_PASSWORD TEXT)"
    }
}

class ProfileTable {
    companion object {
        const val PROFILE_TABLE_NAME = "profiles"
        const val COL_UID = "uid"
        const val COL_FIRST_NAME = "fName"
        const val COL_LAST_NAME = "lName"
        const val COL_MOBILE = "mob"
        const val COL_EMAIL = "email"
        const val createProfileTableStmt = "CREATE TABLE IF NOT EXISTS $PROFILE_TABLE_NAME ($COL_UID INTEGER, $COL_FIRST_NAME TEXT, $COL_LAST_NAME TEXT, $COL_MOBILE TEXT, $COL_EMAIL TEXT)"
    }
}

class AddressTable {
    companion object {
        const val ADDRESS_TABLE_NAME = "addresses"
        const val COL_UID = "uid"
        const val COL_FULL_NAME = "full_name"
        const val COL_MOBILE = "mobile"
        const val COL_PIN_CODE = "pin_code"
        const val COL_ADDRESS = "address"
        const val createAddressTableStmt = "CREATE TABLE IF NOT EXISTS $ADDRESS_TABLE_NAME ($COL_UID INTEGER, $COL_FULL_NAME TEXT, $COL_MOBILE TEXT, $COL_PIN_CODE TEXT, $COL_ADDRESS TEXT)"
    }
}

class WishlistTable {
    companion object {
        const val WISHLIST_TABLE_NAME = "wishlists"
        const val COL_UID = "uid"
        const val COL_PRODUCT_ID = "product_id"
        const val createWishlistTableStmt = "CREATE TABLE IF NOT EXISTS $WISHLIST_TABLE_NAME ($COL_UID INTEGER, $COL_PRODUCT_ID INTEGER)"
    }
}