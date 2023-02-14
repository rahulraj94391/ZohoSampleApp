package com.example.mall.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mall.DB
import com.example.mall.LoginPageActivity
import com.example.mall.MSharedPreferences
import com.example.mall.ModelClass.UserDetailsModel
import com.example.mall.R

private const val TAG = "CommonTag_AccountFragment"

class AccountFragment : Fragment() {
    private lateinit var etFullName: TextView
    private lateinit var etMobile: TextView
    private lateinit var etEMail: TextView
    private lateinit var btnLogout: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var currentProfileDetails: UserDetailsModel? = null

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: called")
        sharedPreferences = context.getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: called")
        val view: View = inflater.inflate(R.layout.fragment_account, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
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
        if (null != currentProfileDetails) return
        val currentUserId = sharedPreferences.getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        if (currentUserId != -1) {
            val db = DB(requireActivity().applicationContext)
            Log.d(TAG, "getProfileDetails: called")
            currentProfileDetails = db.queryProfileData(currentUserId)
            etFullName.text = currentProfileDetails?.userName
            etMobile.text = currentProfileDetails?.mobileNo
            etEMail.text = currentProfileDetails?.email
        }
    }

    private fun logoutUser() {
        sharedPreferences.edit().apply {
            putBoolean(MSharedPreferences.IS_LOGGED_IN, false)
            putInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
            apply()
        }
        startActivity(Intent(activity, LoginPageActivity::class.java))
        requireActivity().finish()
    }


    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: called")
        super.onDetach()
    }
}