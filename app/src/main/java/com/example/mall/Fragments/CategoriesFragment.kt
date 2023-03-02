package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.AllCategoriesAdapter
import com.example.mall.Enum.Category
import com.example.mall.Interface.OnCategoryClickListener
import com.example.mall.ModelClass.AllCategoryModel
import com.example.mall.ModelClass.ProductListModel

private const val TAG = "Common_Tag_CategoryFragment"

class CategoriesFragment : Fragment(), OnCategoryClickListener {
    private lateinit var allCategories: RecyclerView
    private lateinit var categoriesList: MutableList<AllCategoryModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).bottomNavigationView.menu.getItem(1).isChecked = true
        (activity as MainActivity).toolbar.title = ToolbarTitle.ALL_CATEGORIES
        categoriesList = getCategories()
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allCategories = view.findViewById(R.id.rv_all_categories)
        allCategories.adapter = AllCategoriesAdapter(categoriesList, this)
        allCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(category: Category) {
        val listOfProducts: ArrayList<ProductListModel> = DB(requireContext()).queryProductsBasedOnCategory(category)
        val fragment = if (listOfProducts.size > 0) ProductsListViewFragment.newInstance(listOfProducts) else ProductNotAvailable()
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.frag_container, fragment)
            addToBackStack(backStackName)
            commit()
        }
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


