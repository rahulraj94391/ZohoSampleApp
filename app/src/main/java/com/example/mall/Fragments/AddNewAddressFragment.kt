package com.example.mall.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mall.*
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.databinding.FragmentAddNewAddressBinding
import java.util.regex.Pattern


private const val TAG = "CT_AddNewAddressFrag"

class AddNewAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddNewAddressBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var db: DB
    private var uid: Int = -1
    private var oldAddress: DeliveryAddressModel? = null
    private var newAddress: DeliveryAddressModel? = null
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).toolbar.title = ToolbarTitle.ADD_NEW_ADDRESS
        uid = sharedViewModel.uid
        db = DB(requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_address, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sharedViewModel.updateAddressId != -1) {
            fillTextFieldsWithPreviousAddress()
        }

        binding.fullNameField.addTextChangedListener(CustomTextWatcher(binding.fullNameField))
        binding.mobileField.addTextChangedListener(CustomTextWatcher(binding.mobileField))
        binding.pinCodeField.addTextChangedListener(CustomTextWatcher(binding.pinCodeField))
        binding.addressField.addTextChangedListener(CustomTextWatcher(binding.addressField))
        binding.saveAddress.setOnClickListener { confirmInputs() }
    }

    private fun fillTextFieldsWithPreviousAddress() {
        oldAddress = db.getAddress(sharedViewModel.updateAddressId)
        binding.fullName.editText!!.setText(oldAddress!!.fullName)
        binding.mobile.editText!!.setText(oldAddress!!.mobile)
        binding.pinCode.editText!!.setText(oldAddress!!.pinCode)
        binding.address.editText!!.setText(oldAddress!!.address)
    }

    private fun confirmInputs() {
        if (!validateName() or !validateMobile() or !validatePINCode() or !validateAddress()) {
            (requireActivity() as MainActivity).haptics.doubleClick()
            return
        }
        else {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.mobile.windowToken, 0)
            val name = binding.fullName.editText!!.text.toString().trim()
            val mobile = binding.mobile.editText!!.text.toString().trim()
            val pinCode = binding.pinCode.editText!!.text.toString().trim()
            val address = binding.address.editText!!.text.toString().trim()
            newAddress = DeliveryAddressModel(-1, name, mobile, pinCode, address)

            toast = if (sharedViewModel.updateAddressId == -1) {
                db.addNewAddress(uid, newAddress!!)
                (requireActivity() as MainActivity).haptics.light()
                Toast.makeText(requireContext(), "New address saved.", Toast.LENGTH_SHORT)
            }
            else if (oldAddress?.equals(newAddress) == true) {
                Log.d(TAG, "Address compare:\n$oldAddress\n$newAddress")
                (requireActivity() as MainActivity).haptics.doubleClick()
                Toast.makeText(requireContext(), "Nothing updated.", Toast.LENGTH_SHORT)
            }
            else {
                db.updateAddress(uid, sharedViewModel.updateAddressId, newAddress!!)
                (requireActivity() as MainActivity).haptics.light()
                Toast.makeText(requireContext(), "Address updated.", Toast.LENGTH_SHORT)
            }

            if (this::toast.isInitialized) toast.cancel()
            toast.show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.updateAddressId = -1
    }

    private fun validateName(): Boolean {
        val name: String = binding.fullName.editText!!.text.toString().trim()
        if (name.isEmpty()) {
            binding.fullName.error = "Field can't be empty."
            return false
        }
        if (containsSpecialCharacter(name)) {
            binding.fullName.error = "Only alphabets are allowed."
            return false
        }
        binding.fullName.error = null
        return true
    }

    private fun containsSpecialCharacter(name: String): Boolean {
        name.toCharArray().forEach {
            if (it !in 'a'..'z' && it !in 'A'..'Z' && it != ' ') {
                return true
            }
        }
        return false
    }

    private fun validateMobile(): Boolean {
        val mob: String = binding.mobile.editText!!.text.toString().trim()
        return if (mob.isEmpty()) {
            binding.mobile.error = "Field can't be empty."
            false
        }
        else if (mob.length != 10) {
            binding.mobile.error = "Must contains 10 digits"
            false
        }
        else {
            binding.mobile.error = null
            true
        }
    }

    private fun validatePINCode(): Boolean {
        val pin: String = binding.pinCode.editText!!.text.toString().trim()
        val regex = "^[1-9]\\d{2}\\s?\\d{3}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(pin)
        return if (!matcher.matches() || pin.length != 6) {
            binding.pinCode.error = "PIN Invalid"
            false
        }
        else {
            binding.pinCode.error = null
            true
        }
    }

    private fun validateAddress(): Boolean {
        val address = binding.address.editText!!.text.toString().trim()
        return if (address.isEmpty()) {
            binding.address.error = "Address cannot be empty!"
            false
        }
        else if (!address.matches(Regex("^[a-zA-Z0-9 \\-\\\\/,.&']*$"))) {
            binding.address.error = "Allowed special characters are {.}, {'}, {-}, {&}, {,}, {/}, {\\} and whitespaces"
            false
        }
        else {
            binding.address.error = null
            true
        }
    }

    inner class CustomTextWatcher(private val inputField: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            when (inputField.id) {
                R.id.fullNameField -> validateName()
                R.id.mobileField -> validateMobile()
                R.id.pinCodeField -> validatePINCode()
                R.id.addressField -> validateAddress()
            }
        }
    }
}