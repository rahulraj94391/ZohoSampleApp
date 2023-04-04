package com.example.mall

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mall.databinding.ActivityLoginPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "CT_LoginPageActivity"

class LoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var haptics: Haptics
    private lateinit var toast: Toast

    inner class CustomTextWatcher(private val inputField: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            when (inputField.id) {
                R.id.usernameField -> {
                    validateUsername()
                }

                R.id.passwordField -> {
                    validatePassword()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // changes the content color of the system bars
        toast = Toast.makeText(this@LoginPageActivity, "Wrong Credentials.", Toast.LENGTH_SHORT)
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_page)
        sharedPreferences = getSharedPreferences(MSharedPreferences.NAME, MODE_PRIVATE)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary_container)
        binding.btnLogin.setOnClickListener { confirmInputs() }
        binding.usernameField.addTextChangedListener(CustomTextWatcher(binding.usernameField))
        binding.passwordField.addTextChangedListener(CustomTextWatcher(binding.passwordField))
    }

    private fun validateUsername(): Boolean {
        val email: String = binding.textInputUsername.editText!!.text.toString().trim()
        return if (email.isEmpty()) {
            binding.textInputUsername.error = "Field can't be empty."
            false
        }
        else {
            binding.textInputUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password: String = binding.textInputPassword.editText!!.text.toString().trim()
        return if (password.isEmpty()) {
            binding.textInputPassword.error = "Field can't be empty."
            false
        }
        else {
            binding.textInputPassword.error = null
            true
        }
    }

    private fun confirmInputs() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.textInputPassword.windowToken, 0)
        if (!validateUsername() or !validatePassword()) {
            haptics.doubleClick()
            return
        }
        else {
            binding.btnLogin.apply {
                visibility = View.INVISIBLE
                isEnabled = false
            }

            binding.progressIndicator.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                delay(800)
                val db = DB(this@LoginPageActivity)
                val isValidUser = db.isExistingUser(binding.textInputUsername.editText?.text.toString(), binding.textInputPassword.editText?.text.toString())
                if (isValidUser) {
                    haptics.light()
                    sharedPreferences.edit().apply {
                        putBoolean(MSharedPreferences.IS_LOGGED_IN, true)
                        putInt(MSharedPreferences.LOGGED_IN_USER_ID, db.getUserId(binding.textInputUsername.editText!!.text.toString().trim()))
                        apply()
                    }
                    startActivity(Intent(this@LoginPageActivity, MainActivity::class.java))
                    finish()
                }
                else {
                    haptics.doubleClick()
                    if (::toast.isInitialized) {
                        toast.cancel()
                    }
                    toast.show()
                    binding.btnLogin.apply {
                        visibility = View.VISIBLE
                        isEnabled = true
                    }
                }
                binding.progressIndicator.visibility = View.INVISIBLE
            }
        }
    }
}