package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mall.DB
import com.example.mall.ModelClass.ProductListModel
import com.example.mall.R
import com.example.mall.SharedViewModel

private const val ARG_PRODUCT_LIST = "productList"

private const val TAG = "Common_Tag_FiltersFragment"

class FiltersFragment : Fragment() {
    private lateinit var db: DB
    private lateinit var spinner: Spinner
    private lateinit var btnApplyFilter: Button
    private lateinit var minPrice: TextView
    private lateinit var maxPrice: TextView
    private lateinit var listOfProducts: ArrayList<ProductListModel>
    private val sortOptions: Array<String> = arrayOf("None", "Price: High to Low", "Price: Low to High")
    private var minRange: Int = 0
    private var maxRange: Int = Int.MAX_VALUE / 10
    private lateinit var sharedViewModel: SharedViewModel


    companion object {
        fun newInstance() = FiltersFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        arguments?.let {
            listOfProducts = it.getParcelableArrayList(ARG_PRODUCT_LIST)!!
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = DB(requireContext())
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.s_sort)
        minPrice = view.findViewById(R.id.et_min_price)
        maxPrice = view.findViewById(R.id.et_max_price)
        btnApplyFilter = view.findViewById(R.id.btn_apply_filter)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        btnApplyFilter.setOnClickListener {
            applyFilter()
        }
    }

    class ComparatorOne : Comparator<ProductListModel> {
        override fun compare(o1: ProductListModel?, o2: ProductListModel?): Int {
            if (o1 == null || o2 == null) {
                return 0
            }
            return o1.prodPrice.compareTo(o2.prodPrice)
        }
    }

    private fun applyFilter() {
        if (validation()) {
            val filteredList: ArrayList<ProductListModel> = arrayListOf()

            listOfProducts = sharedViewModel.prodList.value!!

            // add item from original List to filtered list within given price range
            for (item in listOfProducts) if (item.prodPrice in minRange..maxRange) filteredList.add(item)

            val selectedText = spinner.selectedItem as String

            // sort High to Low
            if (selectedText == sortOptions[1]) filteredList.sortWith(ComparatorOne().reversed())

            // sort low to high
            else if (selectedText == sortOptions[2]) filteredList.sortWith(ComparatorOne())

            // put filtered ArrayList in bundle
//            val bundle = Bundle().apply {
//                putParcelableArrayList("filteredList", filteredList)
//            }
//            parentFragmentManager.setFragmentResult("reqKeyOfFilteredList", bundle)

            sharedViewModel.prodList.value = filteredList
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    private fun validation(): Boolean {
        val minPrice = minPrice.text.toString()
        val maxPrice = maxPrice.text.toString()

        if (minPrice.isNotBlank() && maxPrice.isNotBlank() && minPrice.toInt() < maxPrice.toInt()) {
            this.minRange = minPrice.toInt()
            this.maxRange = maxPrice.toInt()
            return true
        }
        else if (minPrice.isBlank() && maxPrice.isNotBlank()) {
            maxRange = maxPrice.toInt()
            return true
        }
        else if (minPrice.isNotBlank() && maxPrice.isBlank()) {
            minRange = minPrice.toInt()
            return true
        }
        else {
            Toast.makeText(requireContext(), "Invalid Inputs", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}