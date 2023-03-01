package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.OrderActivityAdapter
import com.example.mall.Enum.PaymentType
import com.example.mall.Interface.CheckoutDescriptionListener
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.ModelClass.PriceDetailsModel

private const val TAG = "Common_Tag_CheckoutDescriptionFrag"

private const val ARG_CART_LIST = "cartList"

class CheckoutDescriptionFragment : Fragment(), CheckoutDescriptionListener {
    private lateinit var rvCheckout: RecyclerView
    private lateinit var adapter: OrderActivityAdapter
    private lateinit var addresses: ArrayList<DeliveryAddressModel>
    private var addressIdx: Int = 0
    private lateinit var sharedViewModel: SharedViewModel
    private var uid: Int = -1
    private lateinit var cartList: ArrayList<CartItemModel>


    companion object {
        fun newInstance(cartList: ArrayList<CartItemModel>) =
            CheckoutDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_CART_LIST, cartList)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        uid = sharedViewModel.uid.value!!
        addressIdx = sharedViewModel.addressId.value!!
        arguments?.let {
            cartList = it.getParcelableArrayList(ARG_CART_LIST)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = ToolbarTitle.CHECKOUT
        return inflater.inflate(R.layout.fragment_checkout_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCheckout = view.findViewById(R.id.rv_checkout)
        addresses = DB(requireContext()).getAddresses(uid)
        adapter = OrderActivityAdapter(cartList, getCheckoutDetails(), addresses[addressIdx], this@CheckoutDescriptionFragment)
        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun getCheckoutDetails(): PriceDetailsModel {
        var totalItemNumber = 0
        var priceBeforeDiscount = 0
        val totalPriceToPay: Int
        for (item in cartList) {
            totalItemNumber += item.quantity
            val itemPrice = item.price
            val itemQty = item.quantity
            priceBeforeDiscount += (itemPrice * itemQty)

        }
//        discount = (priceBeforeDiscount * 0.02).toInt()
        val discount = 0
        totalPriceToPay = priceBeforeDiscount - discount
        return PriceDetailsModel(totalItemNumber, priceBeforeDiscount, discount, totalPriceToPay)
    }

    override fun changeDeliveryAddress() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frag_container, SelectDeliveryAddressFragment.newInstance(addresses))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun redirectToPaymentPortal(id: Int) {
        val payment = when (id) {
            R.id.rb_payment_upi -> PaymentType.UPI
            R.id.rb_payment_card -> PaymentType.CARD
            R.id.rb_payment_pod -> PaymentType.POD
            R.id.rb_payment_EMI -> PaymentType.EMI
            else -> throw java.lang.Exception("Undefined Payment Method Selected")
        }
        DB(requireContext()).confirmOrdersOnPayment(uid, addresses[addressIdx].addressId, payment)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, PaymentConfirmedFragment.newInstance(payment))
            addToBackStack(backStackName)
            commit()
        }
    }

}