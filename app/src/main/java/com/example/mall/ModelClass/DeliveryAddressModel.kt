package com.example.mall.ModelClass

import android.os.Parcel
import android.os.Parcelable

data class DeliveryAddressModel(
    val addressId: Int = -1,
    val fullName: String? = "NOT_SET",
    val mobile: String? = "NOT_SET",
    val pinCode: String? = "NOT_SET",
    val address: String? = "NOT_SET"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(addressId)
        parcel.writeString(fullName)
        parcel.writeString(mobile)
        parcel.writeString(pinCode)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeliveryAddressModel> {
        override fun createFromParcel(parcel: Parcel): DeliveryAddressModel {
            return DeliveryAddressModel(parcel)
        }

        override fun newArray(size: Int): Array<DeliveryAddressModel?> {
            return arrayOfNulls(size)
        }
    }
}