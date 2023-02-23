package com.example.mall.ModelClass

data class OrderDescriptionModel(

    var oid: Int,

    var orderDate: Long,
    var deliveryDate: Long,
    var deliveryStatus: String,
    var paymentType: String,
    var qty: Int,

    var imgURL: String,
    var prodName: String,
    var prodPrice: Int,
    var addressModel: DeliveryAddressModel
)