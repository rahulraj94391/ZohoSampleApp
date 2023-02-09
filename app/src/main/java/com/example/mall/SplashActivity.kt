package com.example.mall

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

//private const val TAG = "SplashActivity_Mall"
private const val TAG = "Common_Tag_Splash"

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary_container)

        sharedPreferences = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE)

        if (sharedPreferences.getBoolean(MSharedPreferences.IS_FIRST_TIME, true)) {
            DB(this@SplashActivity).insertDummyData()
            sharedPreferences.edit().apply {
                putBoolean(MSharedPreferences.IS_FIRST_TIME, false)
                apply()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (isLoggedIn()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginPageActivity::class.java))
            }
            finish()
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(MSharedPreferences.IS_LOGGED_IN, false)
    }
}