package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.SelectDeliveryAddressAdapter
import com.example.mall.Interface.OnNewAddressSelected
import com.example.mall.MainActivity
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.R
import com.example.mall.SharedViewModel

private const val ARG_ADDRESSES = "addresses"

class SelectDeliveryAddressFragment : Fragment(), OnNewAddressSelected {
    private lateinit var addresses: ArrayList<DeliveryAddressModel>
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var newDeliveryAddress: RecyclerView
    private lateinit var adapter: SelectDeliveryAddressAdapter

    companion object {
        fun newInstance(addresses: ArrayList<DeliveryAddressModel>) =
            SelectDeliveryAddressFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ADDRESSES, addresses)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addresses = it.getParcelableArrayList(ARG_ADDRESSES)!!
        }
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = "Select another address"
        return inflater.inflate(R.layout.fragment_select_delivery_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newDeliveryAddress = view.findViewById(R.id.rv_new_delivery_address)
        adapter = SelectDeliveryAddressAdapter(addresses, this)
        newDeliveryAddress.adapter = adapter
        newDeliveryAddress.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun selectedNewAddress(position: Int) {
        // shared view model me new address Id set kar de
        sharedViewModel.addressId.value = position
        activity?.supportFragmentManager?.popBackStack()
    }
}