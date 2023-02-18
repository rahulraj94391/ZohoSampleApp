package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.WishlistAdapter
import com.example.mall.Adapters.WishlistItemClickListener
import com.example.mall.ModelClass.WishlistModel

private const val TAG = "Common_Tag_MyWishlistFragment"

class MyWishlistFragment : Fragment(), WishlistItemClickListener {
    private lateinit var listOfProducts: MutableList<WishlistModel>
    private lateinit var productsRV: RecyclerView
    private lateinit var db: DB
    private var uid: Int = -1
    private lateinit var adapter: WishlistAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        return inflater.inflate(R.layout.fragment_my_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).toolbar.title = "Wishlist"
        super.onViewCreated(view, savedInstanceState)
        productsRV = view.findViewById(R.id.rv_wishlist)
        listOfProducts = db.getWishlistItems(uid)
        adapter = WishlistAdapter(listOfProducts, this)
        productsRV.adapter = adapter
        productsRV.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onItemClicked(position: Int) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(listOfProducts[position].pid))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun onTopBtnClicked(position: Int) {

        val deletedRow = db.deleteItemFromWishlist(uid, listOfProducts[position].pid)
        if (deletedRow > 0) {
            listOfProducts.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onDestroyView() {
        when (activity?.supportFragmentManager?.findFragmentById(R.id.frag_container)) {
            is AccountFragment -> (activity as MainActivity).toolbar.title = "Accounts"
        }
        Log.d(TAG, "onDestroyView: called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: called")
        super.onDetach()
    }
}

