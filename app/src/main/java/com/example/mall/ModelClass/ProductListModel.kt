package com.example.mall.ModelClass

import android.os.Parcel
import android.os.Parcelable

data class ProductListModel(
    var pid: Int,
    var prodName: String?,
    var imgURL: String?,
    var prodPrice: Int,
    var stock: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        return this.pid == (other as ProductListModel).pid
    }

    override fun hashCode(): Int {
        return pid
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pid)
        parcel.writeString(prodName)
        parcel.writeString(imgURL)
        parcel.writeInt(prodPrice)
        parcel.writeInt(stock)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductListModel> {
        override fun createFromParcel(parcel: Parcel): ProductListModel {
            return ProductListModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductListModel?> {
            return arrayOfNulls(size)
        }
    }
}