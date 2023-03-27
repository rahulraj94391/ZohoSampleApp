package com.example.mall

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "CT_LoginPageActivity"

class LoginPageActivity : AppCompatActivity() {
    private lateinit var textInputUsername: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout
    private lateinit var btnLogin: Button
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var haptics: Haptics

    override fun onCreate(savedInstanceState: Bundle?) {
        // changes the content color of the system bars
        if (Build.VERSION.SDK_INT >= 30) {
            window.decorView.windowInsetsController?.apply {
                setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
            }
        }
        else if (Build.VERSION.SDK_INT in 11..29) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        haptics = Haptics(this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        sharedPreferences = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary_container)
        textInputUsername = findViewById(R.id.text_input_username)
        textInputPassword = findViewById(R.id.text_input_password)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_indicator)
        btnLogin.setOnClickListener { confirmInputs() }
    }

    private fun validateUsername(): Boolean {
        val email: String = textInputUsername.editText!!.text.toString().trim()
        return if (email.isEmpty()) {
            textInputUsername.error = "Field can't be empty."
            false
        }
        else {
            textInputUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password: String = textInputPassword.editText!!.text.toString().trim()
        return if (password.isEmpty()) {
            textInputPassword.error = "Field can't be empty."
            false
        }
        else {
            textInputPassword.error = null
            true
        }
    }

    private fun confirmInputs() {
        if (!validateUsername() or !validatePassword()) {
            haptics.heavy()
            return
        }
        else {
            btnLogin.apply {
                visibility = View.INVISIBLE
                isEnabled = false
            }

            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                delay(800)
                val db = DB(this@LoginPageActivity)
                val isValidUser = db.isExistingUser(textInputUsername.editText?.text.toString(), textInputPassword.editText?.text.toString())
                if (isValidUser) {
                    haptics.light()
                    sharedPreferences.edit().apply {
                        putBoolean(MSharedPreferences.IS_LOGGED_IN, true)
                        putInt(MSharedPreferences.LOGGED_IN_USER_ID, db.getUserId(textInputUsername.editText!!.text.toString().trim()))
                        apply()
                    }
                    startActivity(Intent(this@LoginPageActivity, MainActivity::class.java))
                    finish()
                }
                else {
                    haptics.heavy()
                    Toast.makeText(this@LoginPageActivity, "Wrong Credentials.", Toast.LENGTH_LONG).show()
                    btnLogin.apply {
                        visibility = View.VISIBLE
                        isEnabled = true
                    }
                }
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
}