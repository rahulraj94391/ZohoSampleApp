package com.example.mall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var uid = MutableLiveData<Int>()
    var addressId = MutableLiveData<Int>(0)

}