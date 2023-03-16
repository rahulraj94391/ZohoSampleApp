package com.example.mall.Fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.mall.*
import com.example.mall.UserDetailsModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.properties.Delegates

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
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var llAddress: LinearLayout
    private var uid: Int by Delegates.notNull()
    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var mMenuProvider: MenuProvider

    override fun onAttach(context: Context) {
        sharedPreferences = context.getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        uid = sharedViewModel.uid.value!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).apply {
            bottomNavigationView.menu.getItem(2).isChecked = true
            toolbar.title = ToolbarTitle.ACCOUNT
        }
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFullName = view.findViewById(R.id.tv_full_name)
        etMobile = view.findViewById(R.id.tv_mobile_number)
        etEMail = view.findViewById(R.id.tv_email)

        llMyOrder = view.findViewById(R.id.ll_account_my_orders)
        llMyWishlist = view.findViewById(R.id.ll_account_my_wishlist)
        llAddress = view.findViewById(R.id.ll_account_address)
        llContactUs = view.findViewById(R.id.ll_account_contact_us)
        getProfileDetails()
        llMyOrder.setOnClickListener { openMyOrders() }
        llMyWishlist.setOnClickListener { openMyWishlist() }
        llAddress.setOnClickListener { openSavedAddresses() }
        llContactUs.setOnClickListener { openContactUs() }
    }

    private fun openSavedAddresses() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, AddressFragment(), "AddressFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun openContactUs() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, ContactUsFragment(), "ContactUsFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun openMyOrders() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, MyOrdersFragment(), "MyOrdersFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MenuHost).removeMenuProvider(mMenuProvider)

    }

    override fun onStart() {
        mMenuProvider = object : MenuProvider {
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
        super.onStart()
    }

    private fun openMyWishlist() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, MyWishlistFragment(), "MyWishlistFragment")
            addToBackStack(backStackName)
            commit()
        }
    }

    private fun logoutDecisionDialog() {
        val positiveAction = DialogInterface.OnClickListener { _, _ ->
            logoutUser()
        }
        val negativeAction = DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        builder = MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogStyle)
        builder.setMessage("Logout current user ?")
            .setCancelable(true)
            .setPositiveButton("yes", positiveAction)
            .setNegativeButton("no", negativeAction)
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

        if (uid != -1) {
            val db = DB(requireActivity().applicationContext)
            currentProfileDetails = db.userDetailsModel(uid)
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
}