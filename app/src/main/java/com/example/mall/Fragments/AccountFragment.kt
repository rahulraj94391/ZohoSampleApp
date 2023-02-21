package com.example.mall.Fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mall.*
import com.example.mall.ModelClass.UserDetailsModel

private const val TAG = "Common_Tag_AccountFragment"

class AccountFragment : Fragment() {
    private lateinit var etFullName: TextView
    private lateinit var etMobile: TextView
    private lateinit var etEMail: TextView
    private lateinit var btnLogout: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var currentProfileDetails: UserDetailsModel? = null
    private lateinit var tvMyOrder: TextView
    private lateinit var tvMyWishlist: TextView
    private lateinit var tvContactUs: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvMyProfile: TextView
    private lateinit var builder: AlertDialog.Builder

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: called")
        sharedPreferences = context.getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

        tvMyProfile = view.findViewById(R.id.tv_account_my_profile)
        tvMyOrder = view.findViewById(R.id.tv_account_my_orders)
        tvMyWishlist = view.findViewById(R.id.tv_account_my_wishlist)
        tvAddress = view.findViewById(R.id.tv_account_address)
        tvContactUs = view.findViewById(R.id.tv_account_contact_us)


        btnLogout = view.findViewById(R.id.btn_logout)

        getProfileDetails()

        tvMyProfile.setOnClickListener {
            Log.d(TAG, "tvMyProfile clicked")
        }
        tvMyOrder.setOnClickListener { myOrderAction() }
        tvMyWishlist.setOnClickListener { myWishlistAction() }
        tvAddress.setOnClickListener { addressAction() }
        tvContactUs.setOnClickListener { contactUsAction() }
        btnLogout.setOnClickListener { logoutDecisionDialog() }
    }

    private fun addressAction() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, AddressFragment(), "AddressFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun myOrderAction() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, MyOrdersFragment(), "MyOrdersFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun myWishlistAction() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, MyWishlistFragment(), "MyWishlistFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun logoutDecisionDialog() {
        val positive = DialogInterface.OnClickListener() { _, _ ->
            logoutUser()
        }
        val negative = DialogInterface.OnClickListener() { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        builder = AlertDialog.Builder(requireContext())
        builder
            .setMessage("Logout current user ?")
            .setCancelable(true)
            .setPositiveButton("yes", positive)
            .setNegativeButton("no", negative)
            .show()
    }

    private fun setUserDetails() {
        etFullName.text = currentProfileDetails?.userName
        etMobile.text = currentProfileDetails?.mobileNo
        etEMail.text = currentProfileDetails?.email
    }

    private fun getProfileDetails() {
        if (null != currentProfileDetails) {
            setUserDetails()
            return
        }
        val currentUserId = sharedPreferences.getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        Log.d(TAG, "currentUserId = $currentUserId")
        if (currentUserId != -1) {
            val db = DB(requireActivity().applicationContext)
            currentProfileDetails = db.userDetailsModel(currentUserId)
            setUserDetails()
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

    private fun contactUsAction() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, ContactUsFragment(), "ContactUsFragment")
            addToBackStack(backStackName)
            commit()
        }
    }
}