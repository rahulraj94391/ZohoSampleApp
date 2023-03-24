package com.example.mall.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mall.*
import com.example.mall.ModelClass.DeliveryAddressModel
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

private const val TAG = "CT_AddNewAddressFrag"

class AddNewAddressFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fullName: TextInputLayout
    private lateinit var pinCode: TextInputLayout
    private lateinit var address: TextInputLayout
    private lateinit var mobile: TextInputLayout
    private lateinit var btnSaveAddress: Button
    private lateinit var db: DB
    private var uid: Int = -1
    private var oldAddress: DeliveryAddressModel? = null
    private var newAddress: DeliveryAddressModel? = null
    private lateinit var toast: Toast

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
        fullName = view.findViewById(R.id.fullName)
        mobile = view.findViewById(R.id.mobile)
        pinCode = view.findViewById(R.id.pinCode)
        address = view.findViewById(R.id.address)
        if (sharedViewModel.updateAddressId != -1) {
            fillTextFields()
        }
        btnSaveAddress = view.findViewById(R.id.saveAddress)
        btnSaveAddress.setOnClickListener { confirmInputs() }
    }

    private fun fillTextFields() {
        oldAddress = db.getAddress(sharedViewModel.updateAddressId)
        fullName.editText!!.setText(oldAddress!!.fullName)
        mobile.editText!!.setText(oldAddress!!.mobile)
        pinCode.editText!!.setText(oldAddress!!.pinCode)
        address.editText!!.setText(oldAddress!!.address)
    }

    private fun confirmInputs() {
        if (!validateName() or !validateMobile() or !validatePINCode() or !validateAddress()) {
            return
        }
        else {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(this.mobile.windowToken, 0)
            val name = fullName.editText!!.text.toString().trim()
            val mobile = mobile.editText!!.text.toString().trim()
            val pinCode = pinCode.editText!!.text.toString().trim()
            val address = address.editText!!.text.toString().trim()
            newAddress = DeliveryAddressModel(-1, name, mobile, pinCode, address)
            toast = if (sharedViewModel.updateAddressId == -1) {
                db.addNewAddress(uid, newAddress!!)
                Toast.makeText(requireContext(), "New address saved.", Toast.LENGTH_SHORT)
            }
            else if (oldAddress?.equals(newAddress) == true) {
                Log.d(TAG, "Address compare:\n$oldAddress\n$newAddress")
                Toast.makeText(requireContext(), "Nothing updated.", Toast.LENGTH_SHORT)
            }
            else {
                db.updateAddress(uid, sharedViewModel.updateAddressId, newAddress!!)
                Toast.makeText(requireContext(), "Address updated.", Toast.LENGTH_SHORT)
            }
            toast.show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.updateAddressId = -1
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