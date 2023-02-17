package com.example.mall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.OrderActivityAdapter
import com.example.mall.ModelClass.CartItemModel

class OrderActivity : AppCompatActivity() {

    private lateinit var rvCheckout: RecyclerView
    private lateinit var adapter: OrderActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)
        rvCheckout = findViewById(R.id.rv_checkout)
        val cartList: ArrayList<CartItemModel>? = intent.getParcelableArrayListExtra("orderList")
        adapter = OrderActivityAdapter(cartList!!)

        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }
}