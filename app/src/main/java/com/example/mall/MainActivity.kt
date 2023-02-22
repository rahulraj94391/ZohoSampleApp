package com.example.mall

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mall.Fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

//private const val TAG = "MainActivity_Mall"
private const val TAG = "Common_Tag_MainActivity"
const val backStackName = "Main_Back_Stack"

class MainActivity : AppCompatActivity() {
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

        Log.d(TAG, "Saved Instance State = ${savedInstanceState == null}")

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
        else if (fm.findFragmentById(R.id.frag_container) is PaymentConfirmedFragment){}
        else super.onBackPressed()
//        setIndicatorOnBackPress()
    }

//    fun setIndicatorOnBackPress() {
//        val position: Int = when (fm.findFragmentById(R.id.frag_container)) {
//            is HomeFragment -> 0
//            is CategoriesFragment -> 1
//            is AccountFragment -> 2
//            is CartFragment -> 3
//            else -> -1
//        }
//        if (position != -1) bottomNavigationView.menu.getItem(position).isChecked = true
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: called")
        outState.putBoolean("flag", false)
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




