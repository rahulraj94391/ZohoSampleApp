package com.example.mall.Interface


interface OnCartItemClickListener : OnClickListener {
    fun onStartButtonClicked(position: Int)
    fun onEndButtonClicked(position: Int)
}