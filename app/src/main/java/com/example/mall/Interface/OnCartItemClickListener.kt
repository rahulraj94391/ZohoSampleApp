package com.example.mall.Interface

import android.widget.Button


interface OnCartItemClickListener : OnClickListener {
    fun onDeleteClicked(position: Int)
    fun onWishlistClicked(position: Int)
    fun onQuantityIncrease(plus: Button, position: Int, minus: Button)
    fun onQuantityDecrease(minus: Button, position: Int, plus: Button)
    fun changeQtyBtn(plus: Button, position: Int, minus: Button)
}