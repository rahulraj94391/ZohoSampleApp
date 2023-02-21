package com.example.mall.ModelClass

import com.example.mall.Enum.DeliveryStatus
import java.util.Date

data class OrdersModel(
    var uid: Int,
    var pid: Int,
    var qty: Int,
    var deliveryStatus: DeliveryStatus,
    var deliveryDate: Date,
    var orderDate: Date
)