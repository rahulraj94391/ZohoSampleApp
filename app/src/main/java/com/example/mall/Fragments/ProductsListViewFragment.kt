package com.example.mall.Fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.ProductListAdapter
import com.example.mall.BottomSheetDialog.PriceFilter
import com.example.mall.BottomSheetDialog.SortFilter
import com.example.mall.Enum.SortBy
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.ProductListModel
import com.example.mall.R
import com.example.mall.SharedViewModel
import com.example.mall.navigateNextWithDefaultAnim
import com.google.android.material.chip.Chip
import com.google.android.material.divider.MaterialDividerItemDecoration

private const val TAG = "CT_ProdLstViewFrag"

class ProductsListViewFragment : Fragment(), OnClickListener {
    lateinit var chipSortBy: Chip
    lateinit var chipPrice: Chip
    private lateinit var clearAllFilters: TextView
    private lateinit var rvProductList: RecyclerView

    private lateinit var priceFilter: PriceFilter
    private lateinit var sortFilter: SortFilter
    private lateinit var adapter: ProductListAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var listOfProducts: ArrayList<ProductListModel>
    private lateinit var filteredListOfProducts: ArrayList<ProductListModel>

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
        rvProductList = view.findViewById(R.id.rv_product_list)
        chipSortBy = view.findViewById(R.id.chip_sort_by)
        chipPrice = view.findViewById(R.id.chip_price)
        clearAllFilters = view.findViewById(R.id.clear_all_filter)

        priceFilter = PriceFilter()
        sortFilter = SortFilter()

        listOfProducts =
            if (savedInstanceState == null) ArrayList(sharedViewModel.prodList)
            else savedInstanceState.getParcelableArrayList<ProductListModel>("prodList") as ArrayList<ProductListModel>
        filteredListOfProducts = sharedViewModel.filteredList
        adapter = ProductListAdapter(listOfProducts, this)
        rvProductList.adapter = adapter
        rvProductList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.dividerInsetStart = 15
        divider.dividerInsetEnd = 15
        rvProductList.addItemDecoration(divider)

        clearAllFilters.setOnClickListener {
            Log.d(TAG, "CLEAR ALL CLICKED")
            deselectStateChipBackgroundColor(chipPrice)
            deselectStateChipBackgroundColor(chipSortBy)
            listOfProducts.clear()
            listOfProducts.addAll(sharedViewModel.prodList)
            adapter.notifyDataSetChanged()
        }
        chipSortBy.setOnClickListener { sortFilter.show(requireActivity().supportFragmentManager, "sortFilter") }
        chipPrice.setOnClickListener { priceFilter.show(requireActivity().supportFragmentManager, "priceFilter") }
    }


    fun selectStateChipBackgroundColor(chip: Chip) {
        chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary_container))
    }

    fun deselectStateChipBackgroundColor(chip: Chip) {
        chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
    }

    fun applyPriceFilters() {
        val minPriceRange = sharedViewModel.priceRange.first
        val maxPriceRange = sharedViewModel.priceRange.second

        for (item in sharedViewModel.prodList)
            if (item.prodPrice in minPriceRange..maxPriceRange)
                filteredListOfProducts.add(item)

        listOfProducts.clear()
        listOfProducts.addAll(filteredListOfProducts)
        filteredListOfProducts.clear()
        applySortFilter()
        adapter.notifyDataSetChanged()
    }

    fun applySortFilter() {
        when (sharedViewModel.sortMethod) {
            SortBy.LOW_TO_HIGH -> {
                listOfProducts.sortWith(PriceComparator())
            }
            SortBy.HIGH_TO_LOW -> {
                listOfProducts.sortWith(PriceComparator().reversed())
            }
            SortBy.NONE -> {}
        }
        adapter.notifyDataSetChanged()
    }

    fun removeSortFilter() {
        listOfProducts.sortWith(ProductIdComparator())
        adapter.notifyDataSetChanged()
    }

    fun removePriceFilter() {
        with(listOfProducts) {
            clear()
            addAll(sharedViewModel.prodList)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onItemClicked(position: Int) {
        navigateNextWithDefaultAnim(SingleProductDescriptionFragment.newInstance(listOfProducts[position].pid), "SingleProductDescriptionFragment")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("prodList", listOfProducts)
    }
}

class ProductIdComparator : Comparator<ProductListModel> {
    override fun compare(o1: ProductListModel?, o2: ProductListModel?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o1.pid.compareTo(o2.pid)
    }
}

class PriceComparator : Comparator<ProductListModel> {
    override fun compare(o1: ProductListModel?, o2: ProductListModel?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o1.prodPrice.compareTo(o2.prodPrice)
    }
}