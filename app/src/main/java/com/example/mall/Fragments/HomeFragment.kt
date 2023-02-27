package com.example.mall.Fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.HomeAdapter
import com.example.mall.Adapters.HomeOffersAdapter
import com.example.mall.Interface.HomeItemClickListeners
import com.example.mall.ModelClass.ItemImgNamePriceModel

//private const val TAG = "HomeFragment"
private const val TAG = "Common_Tag_RANDOM"


class HomeFragment : Fragment(), HomeItemClickListeners {
    lateinit var rvHome: RecyclerView
    lateinit var adapter: HomeAdapter
    lateinit var db: DB
    private var uid: Int = -1
    lateinit var backInStock: MutableList<ItemImgNamePriceModel>
    lateinit var topSelling: MutableList<ItemImgNamePriceModel>
    lateinit var homeOffersAdapter: HomeOffersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        val prodInInventory = db.itemsInInventory()
        val prodIds = mutableSetOf<Int>()
        while (prodIds.size <= 7) prodIds.add((1..prodInInventory).random())

        val x = prodIds.chunked(4)

        backInStock = db.randomFourProducts(x[0])
        topSelling = db.randomFourProducts(x[1])
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).bottomNavigationView.menu.getItem(0).isChecked = true
        (activity as MainActivity).toolbar.title = "Shopie"
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu_home_frag, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.search_product -> {
                        // TODO: Search query for keyword
                        return true
                    }
                }
                return false
            }
//        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
//        val search = menu?.findItem(R.id.search_product)
//        val searchView: SearchView = search?.actionView as SearchView
//        searchView.queryHint = "Search a product ..."
//        return super.onCreateOptionsMenu(menu)
//    }

        }
        (requireActivity() as MenuHost).addMenuProvider(mMenuProvider, viewLifecycleOwner)
        rvHome = view.findViewById(R.id.rv_home)
        homeOffersAdapter = HomeOffersAdapter(offersImagesResId())
        adapter = HomeAdapter(homeOffersAdapter, backInStock, topSelling, this)
        rvHome.adapter = adapter
        rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun offersImagesResId(): MutableList<Int> {
        val images = mutableListOf<Int>()
        images.add(R.drawable.offer1)
        images.add(R.drawable.offer2)
        images.add(R.drawable.offer3)
        return images
    }

    override fun singleOfferBannerClicked() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(17))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun topSellingCardClicked(index: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(topSelling[index].pid))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun backInStockCardClicked(index: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleProductDescriptionFragment(backInStock[index].pid))
            addToBackStack(backStackName)
            commit()
        }
    }
}