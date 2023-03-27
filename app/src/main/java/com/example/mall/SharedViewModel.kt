package com.example.mall

import androidx.lifecycle.ViewModel
import com.example.mall.Enum.SortBy
import com.example.mall.ModelClass.ProductListModel

class SharedViewModel : ViewModel() {
    var uid: Int = -1
    var addressId = 0
    var prodList = ArrayList<ProductListModel>()
    var filteredList: ArrayList<ProductListModel> = ArrayList(prodList)
    var sortMethod = SortBy.NONE
    var priceRange: Pair<Int, Int> = Pair(0, 999999999)
    var updateAddressId: Int = -1
}