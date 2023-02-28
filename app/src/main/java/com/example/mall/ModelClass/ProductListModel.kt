package com.example.mall.ModelClass

data class ProductListModel(
    var pid:Int,
    var prodName: String,
    var imgURL: String,
    var prodPrice: Int,
    var stock: Int
){

    override fun equals(other: Any?): Boolean {
//        return super.equals(other)
        return this.pid == (other as ProductListModel).pid
    }

    override fun hashCode(): Int {
        return pid
    }
}