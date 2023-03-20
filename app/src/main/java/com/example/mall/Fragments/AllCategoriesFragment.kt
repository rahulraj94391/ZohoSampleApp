package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.AllCategoriesAdapter
import com.example.mall.Enum.Category
import com.example.mall.Interface.OnCategoryClickListener
import com.example.mall.ModelClass.AllCategoryModel
import com.example.mall.ModelClass.ProductListModel
import com.google.android.material.divider.MaterialDividerItemDecoration

private const val TAG = "CT_CategoryFragment"


class AllCategoriesFragment : Fragment(), OnCategoryClickListener {
    private lateinit var allCategories: RecyclerView
    private lateinit var categoriesList: MutableList<AllCategoryModel>
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        categoriesList = getCategories()
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        allCategories = view.findViewById(R.id.rv_all_categories)
        allCategories.adapter = AllCategoriesAdapter(categoriesList, this)
        allCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.dividerInsetStart = 15
        divider.dividerInsetEnd = 15
        allCategories.addItemDecoration(divider)
    }

    override fun onCategoryClicked(category: Category) {
        val listOfProducts: ArrayList<ProductListModel> = DB(requireContext()).queryProductsBasedOnCategory(category)
        Log.d(TAG, "listOfProducts = ${listOfProducts.size}")
        sharedViewModel.prodList = listOfProducts
        navigateNextWithCustomAnim(ProductsListViewFragment.newInstance(), "ProductsListViewFragment")

    }

    private fun getCategories(): MutableList<AllCategoryModel> {
        val categoriesList: MutableList<AllCategoryModel> = mutableListOf()
        categoriesList.add(AllCategoryModel(R.drawable.android_phones, Category.ANDROID_PHONES))
        categoriesList.add(AllCategoryModel(R.drawable.ios_phones, Category.IPHONES))
        categoriesList.add(AllCategoryModel(R.drawable.televisions, Category.TELEVISON))
        categoriesList.add(AllCategoryModel(R.drawable.airconditioner, Category.AIR_CONDITIONER))
        categoriesList.add(AllCategoryModel(R.drawable.headphones, Category.HEADPHONES))
        categoriesList.add(AllCategoryModel(R.drawable.refrigerator, Category.REFRIGERATOR))
        categoriesList.add(AllCategoryModel(R.drawable.earphone, Category.EARPHONES))
        categoriesList.add(AllCategoryModel(R.drawable.all_items, Category.ALL_ITEMS))
        return categoriesList
    }
}


