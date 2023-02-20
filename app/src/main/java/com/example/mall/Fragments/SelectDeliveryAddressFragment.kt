package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.SelectDeliveryAddressAdapter
import com.example.mall.Interface.BindNewAddress
import com.example.mall.Interface.OnNewAddressSelected
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.R

class SelectDeliveryAddressFragment(
    private val addresses: MutableList<DeliveryAddressModel>,
    private val listener: BindNewAddress
) : Fragment(), OnNewAddressSelected {
    private lateinit var newDeliveryAddress: RecyclerView
    private lateinit var adapter: SelectDeliveryAddressAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        listener.displayNewAddress(position)
    }
}