package com.example.mall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mall.ModelClass.ProductListModel

class SharedViewModel : ViewModel() {
    var uid = MutableLiveData<Int>()
    var addressId = MutableLiveData<Int>(0)
    var prodList = MutableLiveData<ArrayList<ProductListModel>>()
}