package com.example.mall.ModelClass

import android.os.Parcel
import android.os.Parcelable

data class CartItemModel(val pid: Int, val productName: String?, val price: Int, val quantity: Int, val thumbnailURL: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pid)
        parcel.writeString(productName)
        parcel.writeInt(price)
        parcel.writeInt(quantity)
        parcel.writeString(thumbnailURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemModel> {
        override fun createFromParcel(parcel: Parcel): CartItemModel {
            return CartItemModel(parcel)
        }

        override fun newArray(size: Int): Array<CartItemModel?> {
            return arrayOfNulls(size)
        }
    }
}