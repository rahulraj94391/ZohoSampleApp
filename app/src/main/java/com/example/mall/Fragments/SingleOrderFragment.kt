package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mall.DB
import com.example.mall.DateUtil
import com.example.mall.ModelClass.OrderDescriptionModel
import com.example.mall.R
import com.example.mall.rupeeString
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

private const val ARG_ORDER_ID = "oid"


class SingleOrderFragment : Fragment() {
    private var oid: Int by Delegates.notNull()
    private lateinit var tvOrderDate: TextView
    private lateinit var tvDeliveryDate: TextView
    private lateinit var tvDeliveryStatus: TextView
    private lateinit var ivProductImg: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductQty: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvPaymentMethod: TextView
    private lateinit var tvShippingName: TextView
    private lateinit var tvShippingPhone: TextView
    private lateinit var tvShippingAddress: TextView
    private lateinit var tvShippingPinCode: TextView
    private lateinit var db: DB
    private lateinit var orderHistory: OrderDescriptionModel

    companion object {
        fun newInstance(oid: Int) =
            SingleOrderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ORDER_ID, oid)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            oid = it.getInt(ARG_ORDER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = DB(requireContext())
        orderHistory = db.getOrderHistory(oid)
        return inflater.inflate(R.layout.fragment_single_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvOrderDate = view.findViewById(R.id.tv_order_date)
        tvDeliveryDate = view.findViewById(R.id.tv_delivery_date)
        tvDeliveryStatus = view.findViewById(R.id.textView3)
        ivProductImg = view.findViewById(R.id.iv_my_order_pic)
        tvProductName = view.findViewById(R.id.tv_order_name)
        tvProductQty = view.findViewById(R.id.tv_my_order_qty)
        tvProductPrice = view.findViewById(R.id.tv_my_order_price)
        tvPaymentMethod = view.findViewById(R.id.tv_payment_type)
        tvShippingName = view.findViewById(R.id.tv_shipping_name)
        tvShippingPhone = view.findViewById(R.id.tv_shipping_phone)
        tvShippingAddress = view.findViewById(R.id.tv_shipping_address)
        tvShippingPinCode = view.findViewById(R.id.tv_shipping_pin_code)


        orderHistory.apply {
            tvOrderDate.text = DateUtil.millisToDate(orderDate)
            tvDeliveryDate.text = DateUtil.millisToDate(deliveryDate)
            tvDeliveryStatus.text = deliveryStatus
            tvProductName.text = prodName
            tvProductQty.text = "Qty: $qty"
            tvProductPrice.text = String().rupeeString(prodPrice * qty)
            tvPaymentMethod.text = "Payment is done using $paymentType"

            addressModel.apply {
                tvShippingName.text = fullName
                tvShippingPhone.text = mobile
                tvShippingAddress.text = address
                tvShippingPinCode.text = pinCode
            }

            Picasso
                .get()
                .load(imgURL)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(ivProductImg)
        }
    }
}