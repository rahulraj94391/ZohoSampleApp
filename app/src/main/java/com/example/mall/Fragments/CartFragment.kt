package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.CartItemsAdapter
import com.example.mall.Adapters.OnCartItemClickListener
import com.example.mall.DB
import com.example.mall.MSharedPreferences
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.R
import com.example.mall.backStackName

class CartFragment() : Fragment(), OnCartItemClickListener {
    private lateinit var cartList: RecyclerView
    private lateinit var cartItemList: MutableList<CartItemModel>
    private lateinit var cartItemsAdapter: CartItemsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = requireActivity()
            .getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
            .getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        cartItemList = DB(requireContext()).getCartItems(userId)
        cartList = view.findViewById(R.id.rv_cart)
        cartList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartItemsAdapter = CartItemsAdapter(cartItemList, this@CartFragment)
        cartList.adapter = cartItemsAdapter
        cartList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onStartButtonClicked(position: Int) {
        val id = requireContext()
            .getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
            .getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)

        val rowsDeleted = DB(requireContext()).deleteItemFromCart(id, cartItemList[position].pid)
        if (rowsDeleted > 0) {
            cartItemList.removeAt(position)
            cartItemsAdapter.notifyItemRemoved(position)
        }
    }

    override fun onEndButtonClicked(position: Int) {
        val uid = requireContext()
            .getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
            .getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        val isMoved = DB(requireContext()).addItemFromCartToWishlist(uid, cartItemList[position].pid)
        cartItemList.removeAt(position)
        cartItemsAdapter.notifyItemRemoved(position)
        Toast.makeText(requireContext(), "Added in wishlist...", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(cartItemList[position].pid, activity as ChangeBottomNavigationStatus))
            addToBackStack(backStackName)
            commit()
        }
    }
}