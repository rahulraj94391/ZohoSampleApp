package com.example.mall

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mall.Database.*
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.ModelClass.UserDetailsModel

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

    fun queryProfileData(uid: Int): UserDetailsModel {
        lateinit var profileData: UserDetailsModel
        val query = "SELECT * FROM ${ProfileTable.PROFILE_TABLE_NAME} WHERE ${ProfileTable.COL_UID} = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            val fullName = cursor.getString(1) + " " + cursor.getString(2)
            val mob = cursor.getString(3)
            val email = cursor.getString(4)
            profileData = UserDetailsModel(fullName, mob, email)
        } else {
            profileData = UserDetailsModel("Update details", "-", "-")
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

    fun getCartItems(userId: Int): MutableList<CartItemModel> {
        val cartItemList: MutableList<CartItemModel> = mutableListOf()
        val query = "SELECT cart.pid, prod_details.prod_name, prod_details.price, cart.quantity, prod_details.imgURL0 FROM cart LEFT JOIN prod_details ON prod_details.pid = cart.pid WHERE uid = ? AND quantity > 0"
        val cursor: Cursor = readableDatabase.rawQuery(query, arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                cartItemList.add(
                    CartItemModel(
                        cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cartItemList
    }

    fun getProductDescription(pid: Int): ProdDescPageModel {
        var prodName: String = "Not specified"
        var prodPrice: Int = Int.MAX_VALUE
        var stock: Int = Int.MAX_VALUE
        val imageURLs: MutableList<String> = mutableListOf()
        val specs: MutableMap<String, String> = mutableMapOf()

        val queryProdDetails = "SELECT * FROM prod_details WHERE pid = ?"
        val querySpecs = "SELECT * FROM prod_specs WHERE pid = ?"

        var cursor: Cursor = readableDatabase.rawQuery(queryProdDetails, arrayOf(pid.toString()))
        if (cursor.moveToFirst()) {
            do {
                prodName = cursor.getString(1)
                prodPrice = cursor.getInt(2)
                stock = cursor.getInt(3)
                imageURLs.add(cursor.getString(4))
                imageURLs.add(cursor.getString(5))
                imageURLs.add(cursor.getString(6))
            } while (cursor.moveToNext())
        }
        cursor.close()

        cursor = readableDatabase.rawQuery(querySpecs, arrayOf(pid.toString()))
        if (cursor.moveToFirst()) {
            do {
                specs[cursor.getString(1)] = cursor.getString(2)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ProdDescPageModel(imageURLs, pid, prodName, prodPrice, stock, specs)
    }

    fun addItemFromCartToWishlist(uid: Int, pid: Int){
        deleteItemFromCart(uid, pid)
        addItemToWishlist(uid, pid)
    }

    fun addItemToWishlist(uid: Int, pid: Int): Boolean{
        val cv = ContentValues().apply {
            put(uid.toString(), pid)
        }
        val insert = writableDatabase.insert(WishlistTable.WISHLIST_TABLE_NAME, null, cv)
        return insert != (-1).toLong()
    }

    fun deleteItemFromCart(uid: Int, pid: Int): Int {
        val whereClause = "uid = ? AND pid = ?"
        val rowsDeleted: Int = writableDatabase.delete(CartTable.CART_TABLE_NAME, whereClause, arrayOf(uid.toString(), pid.toString()))
        return rowsDeleted
    }
}