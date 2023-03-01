package com.example.mall

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

private const val TAG = "Common_Tag_Splash"

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary_container)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (isLoggedIn()) startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else startActivity(Intent(this@SplashActivity, LoginPageActivity::class.java))
            finish()
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(MSharedPreferences.IS_LOGGED_IN, false)
    }
}