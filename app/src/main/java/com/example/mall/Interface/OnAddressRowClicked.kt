package com.example.mall.Interface

import android.view.View

interface OnAddressRowClicked: OnClickListener {
    fun showPopup(position: Int, view: View)
}