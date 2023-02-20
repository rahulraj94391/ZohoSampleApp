package com.example.mall

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mall.Fragments.AccountFragment
import com.example.mall.Fragments.CartFragment
import com.example.mall.Fragments.CategoriesFragment
import com.example.mall.Fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

//private const val TAG = "MainActivity_Mall"
private const val TAG = "Common_Tag_MainActivity"
const val backStackName = "Main_Back_Stack"

class MainActivity : AppCompatActivity(){
    lateinit var toolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fm: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: called")
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)
        fm = supportFragmentManager

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        fm.beginTransaction().apply {
            replace(R.id.frag_container, HomeFragment())
            commit()
        }
        bottomNavViewAndListenerConfig()
    }

    private fun bottomNavViewAndListenerConfig() {
        bottomNavigationView = findViewById(R.id.bnv_bottom_menu_container)
        var fragmentTag: String
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            val currentFrag: Fragment = when (item.itemId) {
                R.id.bnv_category -> CategoriesFragment().also {
                    fragmentTag = "CategoryFragment"
                    toolbar.title = "All Categories"
                }
                R.id.bnv_account -> AccountFragment().also {
                    fragmentTag = "AccountFragment"
                    toolbar.title = "Account"
                }
                R.id.bnv_cart -> CartFragment().also {
                    fragmentTag = "CartFragment"
                    toolbar.title = "Cart"
                }
                else -> HomeFragment().also {
                    fragmentTag = "HomeFragment"
                    toolbar.title = "Shopie"
                }
            }
            fm.beginTransaction().apply {
                replace(R.id.frag_container, currentFrag, fragmentTag)
                addToBackStack(backStackName)
                commit()
            }
            true
        }

        bottomNavigationView.setOnItemReselectedListener {
//         TODO: implement scroll to top position on reselect
//            val toast: String = when (item.itemId) {
//                R.id.bnv_category -> "CategoryFragment()"
//                R.id.bnv_account -> "AccountFragment()"
//                R.id.bnv_cart -> "CartFragment()"
//                else -> "HomeFragment()"
//            }
//            Toast.makeText(this, "$toast reselected", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val search = menu?.findItem(R.id.search_product)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search a product ..."
        // TODO: searchView.setOnQueryTextListener()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (fm.findFragmentById(R.id.frag_container) is HomeFragment && fm.backStackEntryCount > 0) {
            repeat(fm.backStackEntryCount) { fm.popBackStack() }
//            Toast.makeText(this, "Press Back Again to exit", Toast.LENGTH_SHORT).show()
            super.onBackPressed()

        }
        else {
            super.onBackPressed()
        }
        val position: Int = when (fm.findFragmentById(R.id.frag_container)) {
            is HomeFragment -> 0.also {
                toolbar.title = "Shopie"
            }
            is CategoriesFragment -> 1.also {
                toolbar.title = "All Categories"
            }
            is AccountFragment -> 2.also {
                toolbar.title = "Accounts"
            }
            is CartFragment -> 3.also {
                toolbar.title = "Cart"
            }
            else -> -1
        }
        if (position != -1) bottomNavigationView.menu.getItem(position).isChecked = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: called")
    }



}




