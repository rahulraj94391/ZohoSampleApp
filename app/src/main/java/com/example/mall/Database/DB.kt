package com.example.mall

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mall.Database.*
import com.example.mall.Enum.Category
import com.example.mall.Enum.DeliveryStatus
import com.example.mall.Enum.PaymentType
import com.example.mall.ModelClass.*

const val DATABASE_NAME = "shopie.db"

//private const val TAG = "Common_Tag_DB"
private const val TAG = "Common_Tag_RANDOM"

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

    fun userDetailsModel(uid: Int): UserDetailsModel {
        lateinit var profileData: UserDetailsModel
        val query = "SELECT * FROM ${ProfileTable.PROFILE_TABLE_NAME} WHERE ${ProfileTable.COL_UID} = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            val fullName = cursor.getString(1) + " " + cursor.getString(2)
            val mob = cursor.getString(3)
            val email = cursor.getString(4)
            profileData = UserDetailsModel(fullName, mob, email)
        }
        else {
            profileData = UserDetailsModel("Update details", "-", "-")
        }
        cursor.close()
        return profileData
    }

    fun isExistingUser(userID: String, password: String): Boolean {
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

    fun getAddresses(uid: Int): ArrayList<DeliveryAddressModel> {
        val addresses: ArrayList<DeliveryAddressModel> = arrayListOf()
        val query = "SELECT address_id, full_name, mobile, pin_code, address FROM addresses WHERE addresses.uid = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            do {
                addresses.add(
                    DeliveryAddressModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return addresses
    }

    fun getAddressAndPinCode(addressId: Int): DeliveryAddressModel? {
        var address: DeliveryAddressModel? = null
        val query = "SELECT addresses.address, addresses.pin_code FROM addresses WHERE addresses.address_id = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(addressId.toString()))
        if (cursor.moveToFirst()) address = DeliveryAddressModel(address = cursor.getString(0), pinCode = cursor.getString(1))
        cursor.close()
        return address
    }

    fun addNewAddress(uid: Int, newAddress: DeliveryAddressModel): Boolean {
        val cv = ContentValues().apply {
            put(AddressTable.COL_FULL_NAME, newAddress.fullName)
            put(AddressTable.COL_UID, uid)
            put(AddressTable.COL_MOBILE, newAddress.mobile)
            put(AddressTable.COL_PIN_CODE, newAddress.pinCode)
            put(AddressTable.COL_ADDRESS, newAddress.address)
        }
        val insert = writableDatabase.insert(AddressTable.ADDRESS_TABLE_NAME, null, cv)
        return insert != (-1).toLong()
    }


    fun getCartItems(uid: Int): ArrayList<CartItemModel> {
        val cartItemList: ArrayList<CartItemModel> = arrayListOf()
        val query = "SELECT cart.pid, prod_details.prod_name, prod_details.price, cart.quantity, prod_details.imgURL0 FROM cart LEFT JOIN prod_details ON prod_details.pid = cart.pid WHERE uid = ?"
        val cursor: Cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            do {
                cartItemList.add(
                    CartItemModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cartItemList
    }

    fun addItemToCart(uid: Int, pid: Int, quantity: Int): Boolean {
        val cv = ContentValues().apply {
            put(CartTable.COL_UID, uid)
            put(CartTable.COL_PID, pid)
            put(CartTable.COL_QUANTITY, quantity)
        }
        val insert = writableDatabase.insert(CartTable.CART_TABLE_NAME, null, cv)
        return insert != (-1).toLong()
    }


    fun singleProdDesc(pid: Int): ProdDescPageModel {
        var prodName = "Not specified"
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
            do specs[cursor.getString(1)] = cursor.getString(2)
            while (cursor.moveToNext())
        }
        cursor.close()
        return ProdDescPageModel(imageURLs, pid, prodName, prodPrice, stock, specs)
    }

    fun isItemInCart(uid: Int, pid: Int): Boolean {
        val query = "SELECT pid FROM cart WHERE uid = ? AND pid = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString(), pid.toString()))
        var queryPID: Int = -1
        if (cursor.moveToFirst()) {
            queryPID = cursor.getInt(0)
        }
        cursor.close()
        return pid == queryPID
    }

    fun addItemFromCartToWishlist(uid: Int, pid: Int): Boolean {
        val deletedRows = deleteItemFromCart(uid, pid)
        val isInsertSuccess = addItemToWishlist(uid, pid)
        return deletedRows > 0 && isInsertSuccess
    }

    fun checkStocksForItems(items: MutableList<CartItemModel>): MutableList<Int> {
        val itemAvailability: MutableList<Int> = mutableListOf()
        for (item in items) itemAvailability.add(checkProductStock(item.pid))
        return itemAvailability
    }

    fun checkProductStock(pid: Int): Int {
        val query = "SELECT prod_details.stock FROM prod_details WHERE prod_details.pid = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(pid.toString()))
        cursor.moveToFirst()
        val stock = cursor.getInt(0)
        cursor.close()
        return stock
    }


    fun addItemToWishlist(uid: Int, pid: Int): Boolean {
        if (isItemInWishlist(uid, pid)) return true
        val cv = ContentValues().apply {
            put(WishlistTable.COL_UID, uid)
            put(WishlistTable.COL_PID, pid)
        }
        return (-1).toLong() != writableDatabase.insert(WishlistTable.WISHLIST_TABLE_NAME, null, cv)
    }

    fun isItemInWishlist(uid: Int, pid: Int): Boolean {
        val query = "SELECT ${WishlistTable.COL_PID} FROM ${WishlistTable.WISHLIST_TABLE_NAME} WHERE ${WishlistTable.COL_UID} = ? AND ${WishlistTable.COL_PID} = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString(), pid.toString()))
        if (cursor.moveToFirst()) {
            val alreadyInWishlist = cursor.getInt(0) == pid
            cursor.close()
            return alreadyInWishlist
        }
        return false
    }

    fun deleteItemFromCart(uid: Int, pid: Int): Int {
        val whereClause = "uid = ? AND pid = ?"
        return writableDatabase.delete(CartTable.CART_TABLE_NAME, whereClause, arrayOf(uid.toString(), pid.toString()))
    }

    fun deleteItemFromWishlist(uid: Int, pid: Int): Int {
        val whereClause = "uid = ? AND pid = ?"
        return writableDatabase.delete(WishlistTable.WISHLIST_TABLE_NAME, whereClause, arrayOf(uid.toString(), pid.toString()))
    }

    fun searchViewResult(search: String?): MutableSet<ProductListModel> {
        val set: MutableSet<ProductListModel> = mutableSetOf()
        val query = "SELECT pid, prod_name, imgURL0, price, stock FROM prod_details WHERE prod_details.prod_name LIKE '%$search%'"
        val cursor = readableDatabase.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                set.add(
                    ProductListModel(
                        pid = cursor.getInt(0),
                        prodName = cursor.getString(1),
                        imgURL = cursor.getString(2),
                        prodPrice = cursor.getInt(3),
                        stock = cursor.getInt(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return set
    }

//    fun getProductsBetweenPriceRange(min: Int, max: Int): ArrayList<ProductListModel> {
//        val filteredList: ArrayList<ProductListModel> = arrayListOf()
//        val query = "SELECT pid, prod_name, imgURL0, price, stock FROM prod_details WHERE price BETWEEN $min AND $max"
//        val cursor = readableDatabase.rawQuery(query, null)
//        if (cursor.moveToFirst()) {
//            do {
//                filteredList.add(
//                    ProductListModel(
//                        pid = cursor.getInt(0),
//                        prodName = cursor.getString(1),
//                        imgURL = cursor.getString(2),
//                        prodPrice = cursor.getInt(3),
//                        stock = cursor.getInt(4)
//                    )
//                )
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        return filteredList
//    }

    fun queryProductsBasedOnCategory(category: Category): ArrayList<ProductListModel> {
        val baseQuery = "SELECT pid, prod_name, imgURL0, price, stock FROM prod_details"
        val categorySpecificQuery = " WHERE pid IN (SELECT pid FROM category_tags WHERE category_tags.category_tag = ?)"
        val keyword: String = when (category) {
            Category.ANDROID_PHONES -> "android"
            Category.IPHONES -> "iPhone"
            Category.REFRIGERATOR -> "refrigerator"
            Category.AIR_CONDITIONER -> "ac"
            Category.HEADPHONES -> "headphone"
            Category.TELEVISON -> "television"
            Category.EARPHONES -> "earphone"
            else -> "-1"
        }

        var selectionArgs: Array<String>? = null
        val finalQuery = if (keyword != "-1") (baseQuery + categorySpecificQuery).also { selectionArgs = arrayOf(keyword) } else baseQuery
        val cursor = readableDatabase.rawQuery(finalQuery, selectionArgs)
        val products: ArrayList<ProductListModel> = arrayListOf()

        if (cursor.moveToFirst()) {
            do {
                products.add(
                    ProductListModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    fun getWishlistItems(uid: Int): MutableList<ItemImgNamePriceModel> {
        val wishListItems: MutableList<ItemImgNamePriceModel> = mutableListOf()
        val query = "SELECT wishlists.pid, prod_details.prod_name, prod_details.price, prod_details.imgURL0 FROM wishlists LEFT JOIN prod_details ON prod_details.pid = wishlists.pid WHERE uid = ?"
        val cursor: Cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            do wishListItems.add(ItemImgNamePriceModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3)))
            while (cursor.moveToNext())
        }
        cursor.close()
        return wishListItems
    }

    fun confirmOrdersOnPayment(uid: Int, addressId: Int, payment: PaymentType) {
        val cartItems: ArrayList<CartItemModel> = getCartItems(uid)
        for (item in cartItems) {
            val cv = ContentValues().apply {
                put(OrdersTable.COL_UID, uid)
                put(OrdersTable.COL_PID, item.pid)
                put(OrdersTable.COL_QUANTITY, item.quantity)
                put(OrdersTable.COL_DELIVERY_STATUS, DeliveryStatus.IN_TRANSIT.status())
                put(OrdersTable.COL_DELIVERY_DATE, DateUtil.randomDate())
                put(OrdersTable.COL_ADDRESS_ID, addressId)
                put(OrdersTable.COL_ORDER_DATE, DateUtil.currentDate())
                put(OrdersTable.COL_PAID_THROUGH, payment.paymentMethod())
            }
            decrementStock(item.quantity, item.pid)
            writableDatabase.insert(OrdersTable.ORDERS_TABLE_NAME, null, cv)
            deleteItemFromCart(uid, item.pid)
        }
    }

    fun getOrders(uid: Int): MutableList<OrdersModel> {
        val orders: MutableList<OrdersModel> = mutableListOf()
        val query = "SELECT orders.oid, orders.delivery_status, prod_details.imgURL0, prod_details.prod_name, orders.order_date FROM orders LEFT JOIN prod_details ON orders.pid = prod_details.pid WHERE orders.uid = ?"
        val cursor: Cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString()))
        if (cursor.moveToFirst()) {
            do orders.add(OrdersModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4)))
            while (cursor.moveToNext())
        }
        cursor.close()
        return orders
    }

//    fun getMinMaxPriceRange(): Pair<Float, Float> {
//        val query = "SELECT MIN(${ProductDetails.COL_PRICE}), MAX(${ProductDetails.COL_PRICE}) FROM ${ProductDetails.PRODUCT_DETAILS_TABLE_NAME}"
//        val cursor = readableDatabase.rawQuery(query, null)
//        var pair: Pair<Float, Float> = Pair(-1.0f, -1.0f)
//        if (cursor.moveToFirst()) pair = Pair(cursor.getInt(0).toFloat(), cursor.getInt(1).toFloat())
//        cursor.close()
//        Log.d(TAG, "getMinMaxPriceRange: $pair")
//        return pair
//    }


    fun itemsInInventory(): Int {
        val query = "SELECT MAX(prod_details.pid) FROM prod_details"
        val cursor = readableDatabase.rawQuery(query, null)
        var items: Int = -1
        if (cursor.moveToFirst()) items = cursor.getInt(0)
        cursor.close()
        return items
    }

    fun randomProducts(prodIds: List<Int>): MutableList<ItemImgNamePriceModel> {
        val items: MutableList<ItemImgNamePriceModel> = mutableListOf()
        for (itemID in prodIds) items.add(getItemImgNamePrice(itemID))
        return items
    }

    fun decrementStock(quantity: Int, pid: Int) {
        val prevStock: Int = checkProductStock(pid)
        val cv = ContentValues().apply {
            put(ProductDetails.COL_STOCK, (prevStock - quantity))
        }
        val whereClause = "pid = ?"
        val row = writableDatabase.update(ProductDetails.PRODUCT_DETAILS_TABLE_NAME, cv, whereClause, arrayOf(pid.toString()))
        Log.d(TAG, "rows affected = $row")
    }

    private fun getItemImgNamePrice(pid: Int): ItemImgNamePriceModel {
        val query = "SELECT prod_details.pid, prod_details.prod_name, prod_details.price, prod_details.imgURL0 FROM prod_details WHERE pid = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(pid.toString()))
        val model: ItemImgNamePriceModel?
        if (cursor.moveToFirst()) {
            model = ItemImgNamePriceModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3))
        }
        else throw Exception("Cant query prod details")
        cursor.close()
        return model
    }


    fun getOrderHistory(oid: Int): OrderDescriptionModel {
        var orderHistory: OrderDescriptionModel? = null
        val query = "SELECT orders.order_date, orders.delivery_date, orders.delivery_status, orders.paid_through, orders.quantity, prod_details.imgURL0, prod_details.prod_name, prod_details.price, addresses.full_name, addresses.mobile, addresses.pin_code, addresses.address  FROM orders LEFT JOIN prod_details ON orders.pid = prod_details.pid LEFT JOIN addresses ON orders.address_id = addresses.address_id WHERE oid = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(oid.toString()))
        if (cursor.moveToFirst()) {
            orderHistory = OrderDescriptionModel(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getInt(7),
                DeliveryAddressModel(
                    fullName = cursor.getString(8),
                    mobile = cursor.getString(9),
                    pinCode = cursor.getString(10),
                    address = cursor.getString(11)
                )
            )
        }
        cursor.close()
        return orderHistory ?: throw Exception("Unable to query order details for orderId = $oid")
    }

    fun currentItemQuantityInCart(uid: Int, pid: Int): Int {
        val query = "SELECT cart.quantity FROM cart WHERE uid = ? AND pid = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(uid.toString(), pid.toString()))
        cursor.moveToFirst()
        var quantity: Int = cursor.getInt(0)
        cursor.close()
        return quantity
    }

    fun incrementItemQuantityInCart(uid: Int, pid: Int) {
        val whereClause = "uid = ? and pid = ?"
        val cv = ContentValues().apply {
            put(CartTable.COL_QUANTITY, currentItemQuantityInCart(uid, pid) + 1)
        }
        val rows = writableDatabase.update(CartTable.CART_TABLE_NAME, cv, whereClause, arrayOf(uid.toString(), pid.toString()))
        if (rows < 1) Log.e(TAG, "cannot increment quantity for an item in cart with pid = $pid")
    }

    fun decrementItemQuantityInCart(uid: Int, pid: Int) {
        val whereClause = "uid = ? and pid = ?"
        val cv = ContentValues().apply {
            put(CartTable.COL_QUANTITY, currentItemQuantityInCart(uid, pid) - 1)
        }
        val rows = writableDatabase.update(CartTable.CART_TABLE_NAME, cv, whereClause, arrayOf(uid.toString(), pid.toString()))
        if (rows < 1) Log.e(TAG, "cannot decrement quantity for an item in cart with pid = $pid")
    }
}




