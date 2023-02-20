package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.OrderActivityAdapter
import com.example.mall.Adapters.PaymentType
import com.example.mall.DB
import com.example.mall.Interface.CheckoutDescriptionListener
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.ModelClass.PriceDetailsModel
import com.example.mall.OrderActivity
import com.example.mall.R

private const val TAG = "Common_Tag_CheckoutDescriptionFrag"

class CheckoutDescriptionFragment : Fragment(), CheckoutDescriptionListener {
    private var cartList: ArrayList<CartItemModel>? = null
    private lateinit var rvCheckout: RecyclerView
    private lateinit var adapter: OrderActivityAdapter
    var uid: Int = -1
    private lateinit var addresses: MutableList<DeliveryAddressModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCheckout = view.findViewById(R.id.rv_checkout)
        cartList = (activity as OrderActivity).intent.getParcelableArrayListExtra("orderList")
        uid = (activity as OrderActivity).intent.getIntExtra("uid", -1)
        addresses = DB(requireContext()).getAddresses(uid)
        adapter = OrderActivityAdapter(cartList!!, getCheckoutDetails(), addresses[0], this@CheckoutDescriptionFragment)
        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun getCheckoutDetails(): PriceDetailsModel {
        var totalItemNumber: Int = 0
        var priceBeforeDiscount = 0
        var discount: Int = 0
        var totalPriceToPay: Int = 0
        for (item in cartList!!) {
            totalItemNumber += item.quantity
            val itemPrice = item.price
            val itemQty = item.quantity
            priceBeforeDiscount += (itemPrice * itemQty)

        }
        discount = (priceBeforeDiscount * 0.02).toInt()
        totalPriceToPay = priceBeforeDiscount - discount
        return PriceDetailsModel(totalItemNumber, priceBeforeDiscount, discount, totalPriceToPay)
    }

    override fun changeDeliveryAddress() {

    }

    override fun redirectToPaymentPortal(id: Int) {
        val payment = when (id) {
            R.id.rb_payment_upi -> PaymentType.UPI
            R.id.rb_payment_card -> PaymentType.CARD
            R.id.rb_payment_pod -> PaymentType.POD
            R.id.rb_payment_EMI -> PaymentType.EMI
            else -> throw java.lang.Exception("Undefined Payment Method Selected")
        }

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frag_container_checkout, PaymentConfirmedFragment(payment))
            commit()
        }
    }

}