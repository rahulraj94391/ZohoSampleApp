package com.example.mall.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.MainActivity
import com.example.mall.R

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    lateinit var viewpager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).bottomNavigationView.menu.getItem(0).isChecked = true
        (activity as MainActivity).toolbar.title = "Shopie"
        Log.d(TAG, "onCreateView")
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
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
    }

}