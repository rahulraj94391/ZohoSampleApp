package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.WishlistAdapter
import com.example.mall.Adapters.WishlistItemClickListener
import com.example.mall.DB
import com.example.mall.MSharedPreferences
import com.example.mall.ModelClass.WishlistModel
import com.example.mall.R

class MyWishlistFragment : Fragment(), WishlistItemClickListener {
    private lateinit var listOfProducts: MutableList<WishlistModel>
    private lateinit var productsRV: RecyclerView
    private lateinit var db: DB
    private var uid: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        return inflater.inflate(R.layout.fragment_my_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productsRV = view.findViewById(R.id.rv_wishlist)
        listOfProducts = db.getWishlistItems(uid)
        productsRV.adapter = WishlistAdapter(listOfProducts, this)
        productsRV.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onTopBtnClicked(position: Int) {
    }

    override fun onBottomBtnClicked(position: Int) {
    }
}

