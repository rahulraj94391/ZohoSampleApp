package com.example.mall

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
    lateinit var toolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fm: FragmentManager
    private lateinit var sharedViewModel: SharedViewModel
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_nav_bar)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.status_bar_nav_bar)
        fm = supportFragmentManager
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.uid.value = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        if (savedInstanceState == null) {
            fm.beginTransaction().apply {
                replace(R.id.frag_container, HomeFragment())
                commit()
            }
        }
        bottomNavViewAndListenerConfig()

        val backStackListener = FragmentManager.OnBackStackChangedListener {
            val topFragment: Fragment = supportFragmentManager.findFragmentById(R.id.frag_container)!!
            if (topFragment is HomeFragment ||
                topFragment is AllCategoriesFragment ||
                topFragment is AccountFragment ||
                topFragment is CartFragment) {
                bottomNavigationView.visibility = View.VISIBLE
            }
            else bottomNavigationView.visibility = View.GONE

            if (supportFragmentManager.findFragmentById(R.id.frag_container)!! is HomeFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
            else supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        supportFragmentManager.addOnBackStackChangedListener(backStackListener)
    }


    override fun onStart() {
        super.onStart()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
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




