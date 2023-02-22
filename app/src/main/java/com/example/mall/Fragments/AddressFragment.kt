package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.AddressAdapter
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.DeliveryAddressModel

private const val TAG = "Common_Tag_AddressFragment"

class AddressFragment : Fragment(), OnClickListener {
    private lateinit var db: DB
    private var uid: Int = -1
    private lateinit var rvAddresses: RecyclerView
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addresses: MutableList<DeliveryAddressModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = "Saved Addresses"
        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
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
        Log.d(TAG, "Add new Address")
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, AddNewAddressFragment(), "AddNewAddressFragment")
            addToBackStack(backStackName)
            commit()
        }

    }
}