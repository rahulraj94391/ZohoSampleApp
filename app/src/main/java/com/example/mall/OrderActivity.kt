package com.example.mall

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mall.Fragments.CheckoutDescriptionFragment

class OrderActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        toolbar = findViewById(R.id.checkout_toolbar)
        setSupportActionBar(toolbar)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)
//        val cartList: ArrayList<CartItemModel>? = intent.getParcelableArrayListExtra("orderList")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container_checkout, CheckoutDescriptionFragment())
                commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("flag", false)
    }

}