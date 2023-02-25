package com.example.mall.Fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.Adapters.HomeAdapter
import com.example.mall.DB
import com.example.mall.Interface.HomeItemClickListeners
import com.example.mall.MSharedPreferences
import com.example.mall.MainActivity
import com.example.mall.ModelClass.ItemImgNamePriceModel
import com.example.mall.R

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), HomeItemClickListeners {
    lateinit var viewpager: ViewPager2
    lateinit var rvHome: RecyclerView
    lateinit var adapter: HomeAdapter
    lateinit var db: DB
    private var uid: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).bottomNavigationView.menu.getItem(0).isChecked = true
        (activity as MainActivity).toolbar.title = "Shopie"
        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
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
//        // TODO: searchView.setOnQueryTextListener()
//        return super.onCreateOptionsMenu(menu)
//    }

        }
        (requireActivity() as MenuHost).addMenuProvider(mMenuProvider, viewLifecycleOwner)
        rvHome = view.findViewById(R.id.rv_home)

        val backInStock: MutableList<ItemImgNamePriceModel> = db.randomFourProducts(uid)
        val topSelling: MutableList<ItemImgNamePriceModel> = db.randomFourProducts(uid)




        adapter = HomeAdapter(backInStock, topSelling, this)
        rvHome.adapter = adapter
        rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


    }

    override fun seeMoreClicked() {

    }
}