package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.WishlistAdapter
import com.example.mall.Interface.WishlistItemClickListener
import com.example.mall.ModelClass.ItemImgNamePriceModel
import kotlin.properties.Delegates

private const val TAG = "Common_Tag_MyWishlistFragment"

class MyWishlistFragment : Fragment(), WishlistItemClickListener {
    private lateinit var listOfProducts: MutableList<ItemImgNamePriceModel>
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var tvWishlistEmpty: TextView
    private lateinit var adapter: WishlistAdapter
    private lateinit var productsRV: RecyclerView
    private var uid: Int by Delegates.notNull()
    private lateinit var db: DB

    private fun wishListStatus() {
        if (listOfProducts.size == 0) {
            tvWishlistEmpty.visibility = View.VISIBLE
            productsRV.visibility = View.GONE
        }
        else {
            tvWishlistEmpty.visibility = View.GONE
            productsRV.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        uid = sharedViewModel.uid.value!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = ToolbarTitle.MY_WISHLIST
        db = DB(requireContext())
        return inflater.inflate(R.layout.fragment_my_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvWishlistEmpty = view.findViewById(R.id.tv_wishlist_empty)
        productsRV = view.findViewById(R.id.rv_wishlist)
        listOfProducts = db.getWishlistItems(uid)
        wishListStatus()
        adapter = WishlistAdapter(listOfProducts, this)
        productsRV.adapter = adapter
        productsRV.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }


    override fun onItemClicked(position: Int) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(listOfProducts[position].pid))
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
        wishListStatus()
    }
}

