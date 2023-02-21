package com.example.mall.Interface


interface OnCartItemClickListener : OnClickListener {
    fun onDeleteClicked(position: Int)
    fun onWishlistClicked(position: Int)
}