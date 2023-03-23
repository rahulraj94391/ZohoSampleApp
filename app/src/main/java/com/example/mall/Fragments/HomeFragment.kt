package com.example.mall.Fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.HomeAdapter
import com.example.mall.Adapters.HomeOffersAdapter
import com.example.mall.Interface.HomeItemClickListeners
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.ItemImgNamePriceModel
import com.example.mall.ModelClass.ProductListModel

private const val TAG = "Common_Tag_HomeFragment"

class HomeFragment : Fragment(), HomeItemClickListeners, OnClickListener {
    private lateinit var rvHome: RecyclerView
    lateinit var adapter: HomeAdapter
    lateinit var db: DB
    private var uid: Int = -1
    private lateinit var backInStock: MutableList<ItemImgNamePriceModel>
    private lateinit var topSelling: MutableList<ItemImgNamePriceModel>
    private lateinit var homeOffersAdapter: HomeOffersAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mMenuProvider: MenuProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        val prodInInventory = db.itemsInInventory()
        val prodIds = mutableSetOf<Int>()
        while (prodIds.size <= 7) {
            val pid = (1..prodInInventory).random()
            if (db.checkProductStock(pid) > 0)
                prodIds.add(pid)
        }

        val splitSet = prodIds.chunked(4)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        backInStock = db.randomProducts(splitSet[0])
        topSelling = db.randomProducts(splitSet[1])
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvHome = view.findViewById(R.id.rv_home)
        homeOffersAdapter = HomeOffersAdapter(offersImagesResId(), this)
        adapter = HomeAdapter(homeOffersAdapter, backInStock, topSelling, this)
        rvHome.adapter = adapter
        rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onStart() {
        super.onStart()
        mMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu_home_frag, menu)
                val searchFun = menu.findItem(R.id.search_view)
                val searchView: SearchView = searchFun.actionView as SearchView
                searchView.queryHint = "Search product to buy ..."
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val set: MutableSet<ProductListModel> = db.searchViewResult(query?.trim())
                        val prodList: ArrayList<ProductListModel> = arrayListOf()
                        prodList.addAll(set)
                        sharedViewModel.prodList = prodList
                        navigateNextWithDefaultAnim(ProductsListViewFragment.newInstance(), "ProductsListViewFragment")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.search_view -> return true
                }
                return false
            }
        }
        (requireActivity() as MenuHost).addMenuProvider(mMenuProvider, viewLifecycleOwner)
    }

    private fun offersImagesResId(): MutableList<Int> {
        val images = mutableListOf<Int>()
        images.add(R.drawable.offer1)
        images.add(R.drawable.offer2)
        images.add(R.drawable.offer3)
        return images
    }

    override fun singleOfferBannerClicked() {
        navigateNextWithCustomAnim(SingleProductDescriptionFragment.newInstance(17), "SingleProductDescriptionFragment")
    }

    override fun topSellingCardClicked(index: Int) {
        navigateNextWithCustomAnim(SingleProductDescriptionFragment.newInstance(topSelling[index].pid), "SingleProductDescriptionFragment")
    }

    override fun backInStockCardClicked(index: Int) {
        navigateNextWithCustomAnim(SingleProductDescriptionFragment.newInstance(backInStock[index].pid), "SingleProductDescriptionFragment")
    }

    override fun onItemClicked(position: Int) {
        val pid = when (position) {
            0 -> 15
            1 -> 16
            else -> 14
        }
        navigateNextWithCustomAnim(SingleProductDescriptionFragment.newInstance(pid), "SingleProductDescriptionFragment")
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MenuHost).removeMenuProvider(mMenuProvider)
    }
}