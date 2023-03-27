package com.example.mall.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mall.*
import com.example.mall.ModelClass.UserDetailsModel
import com.example.mall.databinding.FragmentAccountBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.properties.Delegates

private const val TAG = "CT_AccountFragment"

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var currentProfileDetails: UserDetailsModel? = null
    private lateinit var sharedViewModel: SharedViewModel
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
        uid = sharedViewModel.uid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileDetails()
        binding.apply {
            myOrders.setOnClickListener { navigateNextWithDefaultAnim(MyOrdersFragment(), "MyOrdersFragment") }
            myWishlist.setOnClickListener { navigateNextWithDefaultAnim(MyWishlistFragment(), "MyWishlistFragment") }
            savedAddresses.setOnClickListener { navigateNextWithDefaultAnim(AddressFragment(), "AddressFragment") }
            contactUs.setOnClickListener { navigateNextWithDefaultAnim(ContactUsFragment(), "ContactUsFragment") }
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

    private fun logoutDecisionDialog() {
        builder = MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogStyle)
        builder.setMessage("Logout current user ?")
            .setCancelable(true)
            .setPositiveButton("yes") { _, _ -> logoutUser() }
            .setNegativeButton("no") { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }

    private fun getProfileDetails() {
        if (null == currentProfileDetails) {
            val db = DB(requireContext())
            currentProfileDetails = db.userDetailsModel(uid)
        }
        binding.apply {
            fullName.text = currentProfileDetails?.userName
            mobileNumber.text = currentProfileDetails?.mobileNo
            eMail.text = currentProfileDetails?.email
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
