package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mall.DB
import com.example.mall.DateUtil
import com.example.mall.ModelClass.OrderDescriptionModel
import com.example.mall.R
import com.example.mall.databinding.FragmentSingleOrderBinding
import com.example.mall.rupeeString
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

private const val ARG_ORDER_ID = "oid"

class SingleOrderFragment : Fragment() {
    private lateinit var binding: FragmentSingleOrderBinding
    private var oid: Int by Delegates.notNull()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        db = DB(requireContext())
        orderHistory = db.getOrderHistory(oid)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            orderDate.text = DateUtil.millisToDate(orderHistory.orderDate)
            deliveryDate.text = DateUtil.millisToDate(orderHistory.deliveryDate)
            productName.text = orderHistory.prodName
            orderQuantity.text = "Qty: ${orderHistory.qty}"
            productPrice.text = String().rupeeString(orderHistory.prodPrice * orderHistory.qty)
            paymentMethod.text = "Payment is done using ${orderHistory.paymentType}"
            deliveryStatus.text = orderHistory.deliveryStatus

            with(orderHistory.addressModel) {
                shippingName.text = this.fullName
                shippingMobileNo.text = this.mobile
                shippingAddress.text = this.address
                shippingPINCode.text = this.pinCode
            }
            Picasso.get().load(orderHistory.imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(productImage)
        }
    }
}