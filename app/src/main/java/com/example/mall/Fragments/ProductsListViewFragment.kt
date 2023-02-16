package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.OnClickListener
import com.example.mall.Adapters.ProductListAdapter
import com.example.mall.ModelClass.ProductListModel
import com.example.mall.R
import com.example.mall.backStackName

class ProductsListViewFragment(private val listOfProducts: MutableList<ProductListModel>) : Fragment(), OnClickListener {
    private lateinit var productList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productList = view.findViewById(R.id.rv_product_list)
        productList.adapter = ProductListAdapter(listOfProducts, this)
        productList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        productList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(listOfProducts[position].pid, activity as ChangeBottomNavigationStatus))
            addToBackStack(backStackName)
            commit()
        }
    }
}