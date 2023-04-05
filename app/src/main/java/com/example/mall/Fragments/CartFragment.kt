package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mall.*
import com.example.mall.Adapters.CartItemsAdapter
import com.example.mall.Interface.OnCartItemClickListener
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.databinding.FragmentCartBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TAG = "CT_CartFragment"

class CartFragment : Fragment(), OnCartItemClickListener {
    private lateinit var toast: Toast
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemList: ArrayList<CartItemModel>
    private lateinit var cartItemsAdapter: CartItemsAdapter
    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var db: DB
    private var uid: Int = -1
    private lateinit var sharedViewModel: SharedViewModel
    private val proceedToCheckoutPage = { navigateNextWithCustomAnim(CheckoutDescriptionFragment.newInstance(cartItemList), "CheckoutDescriptionFragment") }
    private var cartTotal: Int = 0
        set(value) {
            field = value
            binding.cartTotalPrice.text = "Cart total ${rupeeString(cartTotal)}"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = sharedViewModel.uid
        db = DB(requireContext())
        cartItemList = db.getCartItems(uid)
        checkCartStatus()
        cartTotal = calculateCartTotal()
        cartItemsAdapter = CartItemsAdapter(cartItemList, this@CartFragment)

        binding.cartItemsList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartItemsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
        binding.placeOrder.setOnClickListener { placeOrder() }
    }

    private fun placeOrder() {
        val itemAvailability: MutableList<Int> = db.checkStocksForItems(cartItemList)
        val itemWentOutOfStockList: MutableList<Int> = mutableListOf()
        for (i in cartItemList.indices) if (itemAvailability[i] < cartItemList[i].quantity) itemWentOutOfStockList.add(i)
        if (itemWentOutOfStockList.size > 0) showItemOutOfStockDialog(itemWentOutOfStockList)
        else proceedToCheckoutPage()
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

    /*private fun deleteDecisionDialog(position: Int) {
        (requireActivity() as MainActivity).haptics.light()
        builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Delete item ?")
            .setCancelable(true)
            .setPositiveButton("yes") { _, _ -> delete(position) }
            .setNegativeButton("no") { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }*/

    /*private fun wishlistDecisionDialog(position: Int) {
        (requireActivity() as MainActivity).haptics.light()
        builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Move item to wishlist ?")
            .setCancelable(true)
            .setPositiveButton("yes") { _, _ -> addItemToWishlist(position) }
            .setNegativeButton("no") { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }*/

    private fun showItemOutOfStockDialog(itemWentOutOfStockList: MutableList<Int>) {
        (requireActivity() as MainActivity).haptics.heavy()
        builder = MaterialAlertDialogBuilder(requireContext())
        builder
            .setTitle("Item(s) out of stock!")
            .setMessage("Few items went out of stock.\nMove them to wishlist and proceed?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                moveOutOfStockItemsFromCartToWishlist(itemWentOutOfStockList)
                proceedToCheckoutPage()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }

    private fun checkCartStatus() {
        if (cartItemList.size == 0) {
            binding.apply {
                cartEmptyMessage.visibility = View.VISIBLE
                bottomCartLinearLayout.visibility = View.GONE
                cartItemsList.visibility = View.GONE
            }
        }
        else {
            binding.apply {
                cartEmptyMessage.visibility = View.GONE
                bottomCartLinearLayout.visibility = View.VISIBLE
                cartItemsList.visibility = View.VISIBLE
            }
        }
    }

    private fun delete(position: Int) {
        val rowsDeleted = db.deleteItemFromCart(uid, cartItemList[position].pid)
        if (rowsDeleted > 0) {
            val removedItem = cartItemList.removeAt(position)
            cartTotal -= removedItem.price * removedItem.quantity
            cartItemsAdapter.notifyItemRemoved(position)
//            val message: String = "${removedItem.productName} deleted...
            if (this::toast.isInitialized) {
                toast.cancel()
            }
            toast = Toast.makeText(requireContext(), "Item deleted from cart.", Toast.LENGTH_SHORT)
            toast.show()
        }
        checkCartStatus()
    }

    private fun addItemToWishlist(position: Int) {
        val isMoved = db.addItemFromCartToWishlist(uid, cartItemList[position].pid)
        if (isMoved) {
            val removedItem = cartItemList.removeAt(position)
            cartTotal -= removedItem.price * removedItem.quantity
            cartItemsAdapter.notifyItemRemoved(position)
//            val message: String = "${removedItem.productName} added to wishlist..."

            if (this::toast.isInitialized) {
                toast.cancel()
            }
            toast = Toast.makeText(requireContext(), "Item added to wishlist.", Toast.LENGTH_SHORT)
            toast.show()

        }
        checkCartStatus()
    }

    private fun calculateCartTotal(): Int {
        var sum = 0
        cartItemList.forEach {
            sum += it.price * it.quantity
        }
        return sum
    }

    override fun onItemClicked(position: Int) {
        navigateNextWithDefaultAnim(SingleProductDescriptionFragment.newInstance(cartItemList[position].pid), "SingleProductDescriptionFragment")
    }

    override fun changeQtyBtn(plus: Button, position: Int, minus: Button) {
        if (cartItemList[position].quantity in 2..3) {
            minus.isEnabled = true
            plus.isEnabled = true
        }
        else if (cartItemList[position].quantity <= 1) {
            minus.isEnabled = false
        }
        else if (cartItemList[position].quantity >= 4) {
            plus.isEnabled = false
        }

        if (cartItemList[position].quantity == db.checkProductStock(cartItemList[position].pid)) {
            plus.isEnabled = false
        }
    }

    override fun deleteItem(position: Int) {
        delete(position)
//        deleteDecisionDialog(position)
    }

    override fun wishlistItem(position: Int) {
        addItemToWishlist(position)
//        wishlistDecisionDialog(position)
    }

    override fun onQuantityIncrease(plus: Button, position: Int, minus: Button) {
        (requireActivity() as MainActivity).haptics.light()
        cartItemList[position].quantity++
        db.incrementItemQuantityInCart(uid, cartItemList[position].pid)
        changeQtyBtn(plus, position, minus)
        cartTotal = calculateCartTotal()
        cartItemsAdapter.notifyItemChanged(position)
    }

    override fun onQuantityDecrease(minus: Button, position: Int, plus: Button) {
        (requireActivity() as MainActivity).haptics.light()
        cartItemList[position].quantity--
        db.decrementItemQuantityInCart(uid, cartItemList[position].pid)
        changeQtyBtn(plus, position, minus)
        cartTotal = calculateCartTotal()
        cartItemsAdapter.notifyItemChanged(position)
    }
}