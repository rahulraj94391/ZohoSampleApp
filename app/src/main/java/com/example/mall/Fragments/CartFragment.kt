package com.example.mall.Fragments

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.CartItemsAdapter
import com.example.mall.Interface.OnCartItemClickListener
import com.example.mall.ModelClass.CartItemModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

private const val TAG = "CT_CartFragment"

class CartFragment : Fragment(), OnCartItemClickListener {
    private lateinit var rvCartList: RecyclerView
    private lateinit var cartItemList: ArrayList<CartItemModel>
    private lateinit var cartItemsAdapter: CartItemsAdapter
    private lateinit var tvTotalCartPrice: TextView
    private lateinit var btnPlaceOrder: Button
    private var cartTotal: Int = 0
        set(value) {
            field = value
            tvTotalCartPrice.text = "Cart total ₹ $cartTotal"
        }

    private lateinit var bottomCartLinearLayout: LinearLayout
    private lateinit var tvCartEmpty: TextView
    private lateinit var builder: AlertDialog.Builder
    private lateinit var db: DB
    private var uid: Int = -1
    private lateinit var sharedViewModel: SharedViewModel

    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            when (direction) {
                ItemTouchHelper.LEFT -> swipeLeftToDelete(position)
                ItemTouchHelper.RIGHT -> swipeRightToWishlist(position)
            }
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.swipe_delete))
                .addSwipeLeftActionIcon(R.drawable.swipe_delete)

                .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_container))
                .addSwipeRightActionIcon(R.drawable.my_wishlist_24_black)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).apply {
            bottomNavigationView.menu.getItem(3).isChecked = true
            toolbar.title = ToolbarTitle.CART
        }
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCartList = view.findViewById(R.id.rv_cart)
        tvTotalCartPrice = view.findViewById(R.id.tv_cart_total_price)
        btnPlaceOrder = view.findViewById(R.id.btn_place_order)
        bottomCartLinearLayout = view.findViewById(R.id.bottom_cart_linear_layout)
        tvCartEmpty = view.findViewById(R.id.tv_cart_empty)
        uid = sharedViewModel.uid.value!!
        db = DB(requireContext())
        cartItemList = db.getCartItems(uid)
        checkCartStatus()
        cartTotal = calculateCartTotal()
        rvCartList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartItemsAdapter = CartItemsAdapter(cartItemList, this@CartFragment)
        rvCartList.adapter = cartItemsAdapter
        rvCartList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvCartList)
        btnPlaceOrder.setOnClickListener { placeOrder() }
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
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.frag_container, CheckoutDescriptionFragment.newInstance(cartItemList), "paymentSuccessful")
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

    private fun checkCartStatus() {
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

    fun swipeLeftToDelete(position: Int) {
        val rowsDeleted = db.deleteItemFromCart(uid, cartItemList[position].pid)
        if (rowsDeleted > 0) {
            val removedItem = cartItemList.removeAt(position)
            cartTotal -= removedItem.price * removedItem.quantity
            cartItemsAdapter.notifyItemRemoved(position)
            Toast.makeText(requireContext(), "${removedItem.productName} deleted...", Toast.LENGTH_SHORT).show()
        }
        checkCartStatus()
    }

    fun swipeRightToWishlist(position: Int) {
        val isMoved = db.addItemFromCartToWishlist(uid, cartItemList[position].pid)
        if (isMoved) {
            val removedItem = cartItemList.removeAt(position)
            cartTotal -= removedItem.price * removedItem.quantity
            cartItemsAdapter.notifyItemRemoved(position)
            Toast.makeText(requireContext(), "${removedItem.productName} added to wishlist...", Toast.LENGTH_SHORT).show()
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
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(cartItemList[position].pid))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun changeQtyBtn(plus: Button, position: Int, minus: Button) {
        when (cartItemList[position].quantity) {
            1 -> minus.isEnabled = false
            4 -> plus.isEnabled = false
            else -> {
                minus.isEnabled = true
                plus.isEnabled = true
            }
        }

        if (cartItemList[position].quantity == db.checkProductStock(cartItemList[position].pid)) {
            plus.isEnabled = false
        }
    }

    override fun onQuantityIncrease(plus: Button, position: Int, minus: Button) {
        cartItemList[position].quantity++
        db.incrementItemQuantityInCart(uid, cartItemList[position].pid)
        changeQtyBtn(plus, position, minus)
        cartTotal = calculateCartTotal()
        cartItemsAdapter.notifyItemChanged(position)
    }

    override fun onQuantityDecrease(minus: Button, position: Int, plus: Button) {
        cartItemList[position].quantity--
        db.decrementItemQuantityInCart(uid, cartItemList[position].pid)
        changeQtyBtn(plus, position, minus)
        cartTotal = calculateCartTotal()
        cartItemsAdapter.notifyItemChanged(position)
    }
}