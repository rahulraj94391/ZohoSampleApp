package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mall.*
import com.example.mall.ModelClass.DeliveryAddressModel
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

private const val TAG = "Common_Tag_AddNewAddressFragment"

class AddNewAddressFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fullName: TextInputLayout
    private lateinit var pinCode: TextInputLayout
    private lateinit var address: TextInputLayout
    private lateinit var mobile: TextInputLayout
    private lateinit var btnSaveAddress: Button
    private lateinit var db: DB
    private var uid: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = ToolbarTitle.ADD_NEW_ADDRESS
        uid = sharedViewModel.uid.value!!
        db = DB(requireContext())
        return inflater.inflate(R.layout.fragment_add_new_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullName = view.findViewById(R.id.til_full_name)
        mobile = view.findViewById(R.id.til_mobile)
        pinCode = view.findViewById(R.id.til_pin_code)
        address = view.findViewById(R.id.til_address)
        btnSaveAddress = view.findViewById(R.id.btn_save_new_address)
        btnSaveAddress.setOnClickListener { confirmInputs() }
    }

    private fun confirmInputs() {
        if (!validateName() or !validateMobile() or !validatePINCode() or !validateAddress()) {
            return
        }
        else {
            val name = fullName.editText!!.text.toString().trim()
            val mobile = mobile.editText!!.text.toString().trim()
            val pinCode = pinCode.editText!!.text.toString().trim()
            val address = address.editText!!.text.toString().trim()
            val newAddress = DeliveryAddressModel(-1, name, mobile, pinCode, address)
            val success = db.addNewAddress(uid, newAddress)
            Toast.makeText(requireContext(), "New address saved.", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun validateName(): Boolean {
        val name: String = fullName.editText!!.text.toString().trim()

        return if (name.isEmpty()) {
            fullName.error = "Field can't be empty."
            false
        }
        else {
            fullName.error = null
            true
        }
    }

    private fun validateMobile(): Boolean {
        val mob: String = mobile.editText!!.text.toString().trim()
        return if (mob.isEmpty()) {
            mobile.error = "Field can't be empty."
            false
        }
        else if (mob.length != 10) {
            mobile.error = "Must contains 10 digits"
            false
        }
        else {
            mobile.error = null
            true
        }
    }

    private fun validatePINCode(): Boolean {
        val pin: String = pinCode.editText!!.text.toString().trim()
        val regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(pin)
        return if (!matcher.matches()) {
            pinCode.error = "PIN Invalid"
            false
        }
        else if (pin.length != 6) {
            pinCode.error = "PIN Invalid"
            false
        }
        else {
            pinCode.error = null
            true
        }
    }

    private fun validateAddress(): Boolean {
        val address = address.editText!!.text.toString().trim()
        return if (address.isEmpty()) {
            this.address.error = "Address cannot be empty!"
            false
        }
        else if (address.length < 10) {
            this.address.error = "At least 10 character"
            false
        }
        else {
            this.address.error = null
            true
        }
    }
}