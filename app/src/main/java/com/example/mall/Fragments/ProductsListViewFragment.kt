package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.ProductListAdapter
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.ProductListModel
import com.example.mall.R
import com.example.mall.SharedViewModel
import com.example.mall.backStackName

//private const val TAG = "Common_Tag_ProdLstViewFrag"
private const val TAG = "MACBOOK"

class ProductsListViewFragment : Fragment(), OnClickListener {
    private lateinit var productList: RecyclerView
    private lateinit var listLength: TextView
    private lateinit var filters: TextView
    private lateinit var adapter: ProductListAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var listOfProducts: ArrayList<ProductListModel>

    companion object {
        fun newInstance() = ProductsListViewFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filters = view.findViewById(R.id.tv_filter)
        listLength = view.findViewById(R.id.tv_found_x_items)
        productList = view.findViewById(R.id.rv_product_list)


        Log.d(TAG, "listofprdsize = ${sharedViewModel.prodList.value!!.size}")

        listOfProducts = sharedViewModel.prodList.value!!



        adapter = ProductListAdapter(listOfProducts, this)
        productList.adapter = adapter
        productList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        productList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        listLength.text = "Found ${listOfProducts.size} items"

        filters.setOnClickListener() {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.frag_container, FiltersFragment.newInstance())
                addToBackStack(backStackName)
                commit()
            }
        }
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(listOfProducts[position].pid))
            addToBackStack(backStackName)
            commit()
        }
    }
}