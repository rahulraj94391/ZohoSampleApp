package com.example.mall.Fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.mall.*
import com.example.mall.ModelClass.UserDetailsModel

private const val TAG = "Common_Tag_AccountFragment"

class AccountFragment : Fragment() {
    private lateinit var etFullName: TextView
    private lateinit var etMobile: TextView
    private lateinit var etEMail: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var currentProfileDetails: UserDetailsModel? = null
    private lateinit var llMyOrder: LinearLayout
    private lateinit var llMyWishlist: LinearLayout
    private lateinit var llContactUs: LinearLayout
    private lateinit var llAddress: LinearLayout
    private lateinit var builder: AlertDialog.Builder

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: called")
        sharedPreferences = context.getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
        super.onAttach(context)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).bottomNavigationView.menu.getItem(2).isChecked = true
        (activity as MainActivity).toolbar.title = "Accounts"
        Log.d(TAG, "onCreateView: called")
        val view: View = inflater.inflate(R.layout.fragment_account, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu_account_frag, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.logout -> {
                        logoutDecisionDialog()
                        return true
                    }
                }
                return false
            }
        }
        (requireActivity() as MenuHost).addMenuProvider(mMenuProvider, viewLifecycleOwner)
        Log.d(TAG, "onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)

        etFullName = view.findViewById(R.id.tv_full_name)
        etMobile = view.findViewById(R.id.tv_mobile_number)
        etEMail = view.findViewById(R.id.tv_email)

        llMyOrder = view.findViewById(R.id.ll_account_my_orders)
        llMyWishlist = view.findViewById(R.id.ll_account_my_wishlist)
        llAddress = view.findViewById(R.id.ll_account_address)
        llContactUs = view.findViewById(R.id.ll_account_contact_us)


        getProfileDetails()

        llMyOrder.setOnClickListener { myOrderAction() }
        llMyWishlist.setOnClickListener { myWishlistAction() }
        llAddress.setOnClickListener { addressAction() }
        llContactUs.setOnClickListener { contactUsAction() }
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