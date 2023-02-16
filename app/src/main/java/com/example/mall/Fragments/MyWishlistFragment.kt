package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.OnClickListener
import com.example.mall.DB
import com.example.mall.MSharedPreferences
import com.example.mall.ModelClass.ProductListModel
import com.example.mall.R
import com.example.mall.backStackName

class MyWishlistFragment : Fragment(), OnClickListener {
    private lateinit var listOfProducts: MutableList<ProductListModel>
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

    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(listOfProducts[position].pid, activity as ChangeBottomNavigationStatus))
            addToBackStack(backStackName)
            commit()
        }
    }
}

