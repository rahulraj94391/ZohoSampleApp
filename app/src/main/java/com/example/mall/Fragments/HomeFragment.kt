package com.example.mall.Fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.HomeAdapter
import com.example.mall.Adapters.HomeOffersAdapter
import com.example.mall.Interface.HomeItemClickListeners
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.ItemImgNamePriceModel
import com.example.mall.ModelClass.ProductListModel

//private const val TAG = "HomeFragment"
private const val TAG = "Common_Tag_RANDOM"


class HomeFragment : Fragment(), HomeItemClickListeners, OnClickListener {
    private lateinit var rvHome: RecyclerView
    lateinit var adapter: HomeAdapter
    lateinit var db: DB
    private var uid: Int = -1
    private lateinit var backInStock: MutableList<ItemImgNamePriceModel>
    private lateinit var topSelling: MutableList<ItemImgNamePriceModel>
    private lateinit var homeOffersAdapter: HomeOffersAdapter

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

        backInStock = db.randomFourProducts(splitSet[0])
        topSelling = db.randomFourProducts(splitSet[1])
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            bottomNavigationView.menu.getItem(0).isChecked = true
            toolbar.title = ToolbarTitle.HOME
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mMenuProvider = object : MenuProvider {
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

                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            replace(R.id.frag_container, ProductsListViewFragment.newInstance(prodList), "SearchView")
                            addToBackStack(backStackName)
                            commit()
                        }
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
        rvHome = view.findViewById(R.id.rv_home)
        homeOffersAdapter = HomeOffersAdapter(offersImagesResId(), this)
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
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(17))
            addToBackStack(backStackName)
            commit()
        }
    }


    override fun topSellingCardClicked(index: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(topSelling[index].pid))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun backInStockCardClicked(index: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(backInStock[index].pid))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun onItemClicked(position: Int) {
        val pid = when (position) {
            0 -> 15
            1 -> 16
            else -> 14
        }

        requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.frag_container, SingleProductDescriptionFragment.newInstance(pid))
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}