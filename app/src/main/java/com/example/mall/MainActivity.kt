package com.example.mall

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mall.Fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

//private const val TAG = "MainActivity_Mall"
private const val TAG = "Common_Tag_MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private val backStackName = "Main_Back_Stack"
    private lateinit var fm: FragmentManager
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: called")

        fm = supportFragmentManager

        configNavigationViewListener()
        setupStatusBarAndNavigationBar()
        bottomNavViewAndListenerConfig()
        configToolbarAndNavDrawer()
        setupInitialHomeFragment()
    }

    private fun configNavigationViewListener() {
        navView = findViewById(R.id.nav_view)
//        navView.menu.getItem(2).isChecked = true

        val navViewListener = NavigationView.OnNavigationItemSelectedListener {
            val fragmentInstance: Fragment = when (it.itemId) {
                R.id.nav_my_order -> MyOrdersFragment()
                R.id.nav_my_wishlist -> MyWishlistFragment()
                R.id.nav_FAQs -> FaqsFragment()
                R.id.nav_contact_us -> ContactUsFragment()
                R.id.nav_legal_terms -> LegalTermsFragment()
                else -> TestingZoneFragment()
            }
            fm.beginTransaction().apply {
                replace(R.id.frag_container, fragmentInstance)
                addToBackStack(backStackName)
                commit()
            }
            drawer.closeDrawer(Gravity.LEFT)
            true
        }
        navView.setNavigationItemSelectedListener(navViewListener)
    }

    private fun setupStatusBarAndNavigationBar() {
        /* Setting the color of the status bar to darker shade of purple */
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)

        /* set system navigation color */
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)
    }

    private fun setupInitialHomeFragment() {
//        bottomNavigationView.menu.getItem(0).isChecked = true
//        bottomNavigationView.menu.getItem(0).icon = resources.getDrawable(R.drawable.home_filled_24, null)
        fm.beginTransaction().apply {
            replace(R.id.frag_container, HomeFragment())
            commit()
        }
    }

    private fun configToolbarAndNavDrawer() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)

        // attach hamburger with navigation drawer
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun bottomNavViewAndListenerConfig() {
        bottomNavigationView = findViewById(R.id.bnv_bottom_menu_container)
        bottomNavigationView.setOnItemSelectedListener() { item: MenuItem ->
            val currentFrag: Fragment = when (item.itemId) {
                R.id.bnv_category -> CategoryFragment()
                R.id.bnv_account -> AccountFragment()
                R.id.bnv_cart -> CartFragment()
                else -> HomeFragment()
            }
            fm.beginTransaction().apply {
                replace(R.id.frag_container, currentFrag)
                addToBackStack(backStackName)
                commit()
            }
            true
        }

        bottomNavigationView.setOnItemReselectedListener { item: MenuItem ->
            // TODO: implement scroll to top position on reselect
            val toast: String = when (item.itemId) {
                R.id.bnv_category -> "CategoryFragment()"
                R.id.bnv_account -> "AccountFragment()"
                R.id.bnv_cart -> "CartFragment()"
                else -> "HomeFragment()"
            }
            Toast.makeText(this, "$toast reselected", Toast.LENGTH_SHORT).show();
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (fm.findFragmentById(R.id.frag_container) is HomeFragment && fm.backStackEntryCount > 0) {
            repeat(fm.backStackEntryCount) { fm.popBackStack() }
            Toast.makeText(this, "Press Back Again to exit", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
        val topFragment: Int = when (fm.findFragmentById(R.id.frag_container)) {
            is HomeFragment -> 0
            is CategoryFragment -> 1
            is AccountFragment -> 2
            is CartFragment -> 3
            else -> -1
        }
        if (topFragment != -1) bottomNavigationView.menu.getItem(topFragment).isChecked = true
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



