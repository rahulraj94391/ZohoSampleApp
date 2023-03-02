package com.example.mall

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.mall.Fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "Common_Tag_MainActivity"
const val backStackName = "Main_Back_Stack"

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fm: FragmentManager
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary)
        fm = supportFragmentManager
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.uid.value = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        if (savedInstanceState == null) {
            fm.beginTransaction().apply {
                replace(R.id.frag_container, HomeFragment(), "baseHomeFrag")
                commit()
            }
        }

        bottomNavViewAndListenerConfig()
    }

    private fun bottomNavViewAndListenerConfig() {
        bottomNavigationView = findViewById(R.id.bnv_bottom_menu_container)
        var fragmentTag: String
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            val currentFrag: Fragment = when (item.itemId) {
                R.id.bnv_category -> CategoriesFragment().also { fragmentTag = "CategoryFragment" }
                R.id.bnv_account -> AccountFragment().also { fragmentTag = "AccountFragment" }
                R.id.bnv_cart -> CartFragment().also { fragmentTag = "CartFragment" }
                else -> HomeFragment().also { fragmentTag = "HomeFragment" }
            }
            fm.beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.frag_container, currentFrag, fragmentTag)
                addToBackStack(backStackName)
                commit()
            }
            true
        }

        bottomNavigationView.setOnItemReselectedListener {
        }
    }


    override fun onBackPressed() {
        if (fm.findFragmentById(R.id.frag_container) is HomeFragment && fm.backStackEntryCount > 0) {
            fm.popBackStack(0, 1)
            super.onBackPressed()
        }
        else if (fm.findFragmentById(R.id.frag_container) is PaymentConfirmedFragment) {
        }
        else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("flag", false)
        super.onSaveInstanceState(outState)
    }
}




