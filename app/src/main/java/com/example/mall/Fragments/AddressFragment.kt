package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.AddressAdapter
import com.example.mall.DB
import com.example.mall.Interface.OnAddressRowClicked
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.R
import com.example.mall.SharedViewModel
import com.example.mall.navigateNextWithDefaultAnim

private const val TAG = "CT_AddressFrag"

class AddressFragment : Fragment(), OnAddressRowClicked {
    private lateinit var db: DB
    private var uid: Int = -1
    private lateinit var rvAddresses: RecyclerView
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addresses: MutableList<DeliveryAddressModel>
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = DB(requireContext())
        uid = sharedViewModel.uid.value!!
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addresses = db.getUserAddresses(uid)
        rvAddresses = view.findViewById(R.id.rv_saved_address)
        addressAdapter = AddressAdapter(addresses, this)
        rvAddresses.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    override fun showPopup(position: Int, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_edit_address, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.edit_address -> updateAddress(position)
                R.id.delete_address -> deleteAddress(position)
            }
            true
        }
        popupMenu.show()
    }

    private fun deleteAddress(position: Int) {
        val deletedAddress = addresses.removeAt(position)
        addressAdapter.notifyItemRemoved(position)
        db.deleteAddress(deletedAddress.addressId)
    }

    private fun updateAddress(position: Int) {
        sharedViewModel.updateAddressId = addresses[position].addressId
        navigateNextWithDefaultAnim(AddNewAddressFragment(), "AddNewAddressFragment")
    }

    override fun onItemClicked(position: Int) {
        navigateNextWithDefaultAnim(AddNewAddressFragment(), "AddNewAddressFragment")
    }
}