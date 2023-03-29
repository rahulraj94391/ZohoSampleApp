package com.example.mall

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.mall.Fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "CT_MainActivity"
const val backStackName = "Main_Back_Stack"

class MainActivity : AppCompatActivity() {
    lateinit var haptics: Haptics
    lateinit var toolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fm: FragmentManager
    private lateinit var sharedViewModel: SharedViewModel
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast

    private fun checkUpAndBottomNavBar() {
        val topFragment: Fragment = supportFragmentManager.findFragmentById(R.id.frag_container)!!

        // setup the visibility of the bottom navigation bar
        if (topFragment is HomeFragment ||
            topFragment is AllCategoriesFragment ||
            topFragment is AccountFragment ||
            topFragment is CartFragment) {
            bottomNavigationView.visibility = View.VISIBLE
        }
        else bottomNavigationView.visibility = View.GONE

        // change title
        when (topFragment) {
            is AccountFragment -> {
                bottomNavigationView.menu.getItem(2).isChecked = true
                toolbar.title = ToolbarTitle.ACCOUNT
            }
            is AddressFragment -> toolbar.title = ToolbarTitle.SAVED_ADDRESS
            is AllCategoriesFragment -> {
                bottomNavigationView.menu.getItem(1).isChecked = true
                toolbar.title = ToolbarTitle.ALL_CATEGORIES
            }
            is CartFragment -> {
                bottomNavigationView.menu.getItem(3).isChecked = true
                toolbar.title = ToolbarTitle.CART
            }
            is CheckoutDescriptionFragment -> toolbar.title = ToolbarTitle.CHECKOUT
            is ContactUsFragment -> toolbar.title = ToolbarTitle.CONTACT_US
            is HomeFragment -> {
                bottomNavigationView.menu.getItem(0).isChecked = true
                toolbar.apply {
                    visibility = View.VISIBLE
                    title = ToolbarTitle.HOME
                }
            }
            is MyOrdersFragment -> toolbar.title = ToolbarTitle.MY_ORDERS
            is MyWishlistFragment -> toolbar.title = ToolbarTitle.MY_WISHLIST
            is PaymentConfirmedFragment -> {
                bottomNavigationView.visibility = View.GONE
                toolbar.visibility = View.GONE
            }
            is SelectDeliveryAddressFragment -> toolbar.title = ToolbarTitle.SELECT_ANOTHER_ADDRESS
        }


        // hide and show the Navigate Up button in the action bar
        if (supportFragmentManager.findFragmentById(R.id.frag_container)!! is HomeFragment) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        else supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*if (Build.VERSION.SDK_INT >= 30) {
            window.decorView.windowInsetsController?.apply {
                setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
            }
        }
        else if (Build.VERSION.SDK_INT in 11..29) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }*/
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_nav_bar)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.status_bar_nav_bar)
        haptics = Haptics(this@MainActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        fm = supportFragmentManager
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.uid = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        if (savedInstanceState == null) {
            fm.beginTransaction().apply {
                replace(R.id.frag_container, HomeFragment())
                commit()
            }
        }
        bottomNavViewAndListenerConfig()
        supportFragmentManager.addOnBackStackChangedListener { checkUpAndBottomNavBar() }
    }


    override fun onStart() {
        super.onStart()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        checkUpAndBottomNavBar()
    }


    private fun bottomNavViewAndListenerConfig() {
        bottomNavigationView = findViewById(R.id.bnv_bottom_menu_container)
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            val currentFrag: Fragment = when (item.itemId) {
                R.id.bnv_category -> AllCategoriesFragment()
                R.id.bnv_account -> AccountFragment()
                R.id.bnv_cart -> CartFragment()
                else -> HomeFragment()
            }
            fm.beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.frag_container, currentFrag)
                addToBackStack(backStackName)
                commit()
            }
            true
        }
        bottomNavigationView.setOnItemReselectedListener {}
    }


    override fun onBackPressed() {
        if (fm.findFragmentById(R.id.frag_container) is HomeFragment) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish()
                toast.cancel()
            }
            else {
                backPressedTime = System.currentTimeMillis()
                toast = Toast.makeText(this, "Press Back Again", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        else if (fm.findFragmentById(R.id.frag_container) is PaymentConfirmedFragment) {
            // do nothing
        }
        else super.onBackPressed()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("flag", false)
        super.onSaveInstanceState(outState)
    }
}




