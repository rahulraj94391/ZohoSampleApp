package com.example.mall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mall.ModelClass.CartItemModel

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)
        val cartList: ArrayList<CartItemModel>? = intent.getParcelableArrayListExtra("orderList")


    }
}