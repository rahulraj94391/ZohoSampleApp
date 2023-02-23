package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mall.R

class SingleOrderFragment() : Fragment() {
    private lateinit var tvOrderDate: TextView
    private lateinit var tvDeliveryDate: TextView
    private lateinit var tvDeliveryStatus: TextView
    private lateinit var ivProductImg: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductQty: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvPaymentMethod: TextView
    private lateinit var tvShippingAddress: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_single_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvOrderDate = view.findViewById(R.id.tv_order_date)
        tvDeliveryDate = view.findViewById(R.id.tv_delivery_date)
        tvDeliveryStatus = view.findViewById(R.id.textView3)
        ivProductImg  = view.findViewById(R.id.iv_my_order_pic)
        tvProductName = view.findViewById(R.id.tv_order_name)
        tvProductQty = view.findViewById(R.id.tv_my_order_qty)
        tvProductPrice = view.findViewById(R.id.tv_my_order_price)
        tvPaymentMethod = view.findViewById(R.id.tv_payment_type)
        tvShippingAddress = view.findViewById(R.id.tv_shipping_address)







    }

}