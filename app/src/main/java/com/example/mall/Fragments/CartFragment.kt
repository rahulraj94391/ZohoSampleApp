package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.DB
import com.example.mall.MSharedPreferences
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.R
import com.example.mall.Adapters.CartItemsAdapter

class CartFragment : Fragment() {
    private lateinit var cartList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = requireActivity()
            .getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
            .getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        val mutableList: MutableList<CartItemModel> = DB(requireContext()).getCartItems(userId)
        cartList = view.findViewById(R.id.rv_cart)
        cartList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartList.adapter = CartItemsAdapter(mutableList)
    }
}