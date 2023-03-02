package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.AddressAdapter
import com.example.mall.Adapters.SALES_OFFERS
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.DeliveryAddressModel

private const val TAG = "Common_Tag_AddressFragment"

class AddressFragment : Fragment(), OnClickListener {
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
        (activity as MainActivity).toolbar.title = ToolbarTitle.SAVED_ADDRESS
        db = DB(requireContext())
        uid = sharedViewModel.uid.value!!
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addresses = db.getAddresses(uid)
        rvAddresses = view.findViewById(R.id.rv_saved_address)
        addressAdapter = AddressAdapter(addresses, this)
        rvAddresses.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, AddNewAddressFragment(), "AddNewAddressFragment")
            addToBackStack(backStackName)
            commit()
        }

    }
}