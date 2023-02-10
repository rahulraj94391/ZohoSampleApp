package com.example.mall.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mall.DB
import com.example.mall.LoginPageActivity
import com.example.mall.MSharedPreferences
import com.example.mall.R

class AccountFragment : Fragment() {
    private lateinit var etFullName: TextView
    private lateinit var etMobile: TextView
    private lateinit var etEMail: TextView
    private lateinit var btnLogout: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        sharedPreferences = context.getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_account, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etFullName = view.findViewById(R.id.tv_full_name)
        etMobile = view.findViewById(R.id.tv_mobile_number)
        etEMail = view.findViewById(R.id.tv_email)
        btnLogout = view.findViewById(R.id.btn_logout)
        getProfileDetails()

        btnLogout.setOnClickListener() {
            logoutUser()
        }
    }

    private fun getProfileDetails() {
        val currentUserId = sharedPreferences.getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        if (currentUserId != -1) {
            val db = DB(activity!!.applicationContext)
            val profileDetails: Array<String> = db.queryProfileData(currentUserId)
            etFullName.text = profileDetails[0]
            etMobile.text = profileDetails[1]
            etEMail.text = profileDetails[2]
        }
    }

    private fun logoutUser() {
        sharedPreferences.edit().apply {
            putBoolean(MSharedPreferences.IS_LOGGED_IN, false)
            putInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
            apply()
        }
        activity!!.finish()
        startActivity(Intent(activity, LoginPageActivity::class.java))
    }
}