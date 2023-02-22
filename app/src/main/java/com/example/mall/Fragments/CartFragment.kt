package com.example.mall.Fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.CartItemsAdapter
import com.example.mall.Interface.OnCartItemClickListener
import com.example.mall.ModelClass.CartItemModel

private const val TAG = "Common_Tag_CartFragment"

class CartFragment : Fragment(), OnCartItemClickListener {
    private lateinit var rvCartList: RecyclerView
    private lateinit var cartItemList: ArrayList<CartItemModel>
    private lateinit var cartItemsAdapter: CartItemsAdapter
    private lateinit var totalCartPrice: TextView
    private lateinit var btnPlaceOrder: Button
    private var cartTotal: Int = 0
    private lateinit var bottomCartLinearLayout: LinearLayout
    private lateinit var tvCartEmpty: TextView
    private lateinit var builder: AlertDialog.Builder
    private lateinit var db: DB
    private var uid: Int = -1

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).bottomNavigationView.menu.getItem(3).isChecked = true
        (activity as MainActivity).toolbar.title = "Cart"

        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: called")

        rvCartList = view.findViewById(R.id.rv_cart)
        totalCartPrice = view.findViewById(R.id.tv_cart_total_price)
        btnPlaceOrder = view.findViewById(R.id.btn_place_order)
        bottomCartLinearLayout = view.findViewById(R.id.bottom_cart_linear_layout)
        tvCartEmpty = view.findViewById(R.id.tv_cart_empty)

        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        db = DB(requireContext())

        cartItemList = db.getCartItems(uid)

        cartStatus()

        totalCartPrice.text = "Cart total ₹" + calculateCartTotal().toString()

        rvCartList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartItemsAdapter = CartItemsAdapter(cartItemList, this@CartFragment)
        rvCartList.adapter = cartItemsAdapter
        rvCartList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        val itemAvailability: MutableList<Int> = db.checkStocksForItems(cartItemList)
        val itemWentOutOfStockList: MutableList<Int> = mutableListOf()
        for (i in cartItemList.indices) if (itemAvailability[i] == 0) itemWentOutOfStockList.add(i)
        if (itemWentOutOfStockList.size > 0) showItemOutOfStockDialog(itemWentOutOfStockList)
        else proceedToCheckoutPage()
    }

    private fun proceedToCheckoutPage() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, CheckoutDescriptionFragment(uid, cartItemList), "paymentSuccessful")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun moveOutOfStockItemsFromCartToWishlist(itemWentOutOfStockList: MutableList<Int>) {
        for (i in itemWentOutOfStockList) {
            val success = db.addItemFromCartToWishlist(uid, cartItemList[i].pid)
            if (success) {
                cartItemList.removeAt(i)
                cartItemsAdapter.notifyItemRemoved(i)
            }
        }
    }

    private fun showItemOutOfStockDialog(itemWentOutOfStockList: MutableList<Int>) {
        val positive = DialogInterface.OnClickListener { _, _ ->
            moveOutOfStockItemsFromCartToWishlist(itemWentOutOfStockList)
            proceedToCheckoutPage()

        }
        val negative = DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("Item(s) out of stock!")
            .setMessage("Few items went out of stock.\nMove them to wishlist and proceed?")
            .setCancelable(false)
            .setPositiveButton("Yes", positive)
            .setNegativeButton("Cancel", negative)
            .show()
    }

    private fun cartStatus() {
        if (cartItemList.size == 0) {
            tvCartEmpty.visibility = View.VISIBLE
            bottomCartLinearLayout.visibility = View.GONE
            rvCartList.visibility = View.GONE
        }
        else {
            tvCartEmpty.visibility = View.GONE
            bottomCartLinearLayout.visibility = View.VISIBLE
            rvCartList.visibility = View.VISIBLE
        }
    }

    override fun onDeleteClicked(position: Int) {
        val rowsDeleted = db.deleteItemFromCart(uid, cartItemList[position].pid)
        if (rowsDeleted > 0) {
            val removedItem = cartItemList.removeAt(position)
            cartTotal -= removedItem.price * removedItem.quantity
            totalCartPrice.text = cartTotal.toString()
            cartItemsAdapter.notifyItemRemoved(position)
        }
        if (cartItemList.size == 0) cartStatus()

    }

    private fun calculateCartTotal(): Int {
        Log.d(TAG, "calculateCartTotal: called")
        var sum = 0
        for (item in cartItemList) {
            sum += item.price * item.quantity
        }
        return sum
    }

    override fun onWishlistClicked(position: Int) {
        val isMoved = db.addItemFromCartToWishlist(uid, cartItemList[position].pid)
        if (isMoved) {
            val removedItem = cartItemList.removeAt(position)
            cartTotal -= removedItem.price * removedItem.quantity
            totalCartPrice.text = cartTotal.toString()
            cartItemsAdapter.notifyItemRemoved(position)
            Toast.makeText(requireContext(), "Added in wishlist...", Toast.LENGTH_SHORT).show()
        }
        if (cartItemList.size == 0) {
            cartStatus()
        }
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(cartItemList[position].pid))
            addToBackStack(backStackName)
            commit()
        }
    }
}