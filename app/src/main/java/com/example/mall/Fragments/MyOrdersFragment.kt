package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.MyOrdersAdapter
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.OrdersModel
import kotlin.properties.Delegates

class MyOrdersFragment : Fragment(), OnClickListener {
    private lateinit var ordersList: MutableList<OrdersModel>
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var rvOrdersList: RecyclerView
    private lateinit var adapter: MyOrdersAdapter
    private lateinit var db: DB
    private var uid: Int by Delegates.notNull()
    private lateinit var tvOrdersEmpty: TextView


    private fun ordersEmptyStatus() {
        if (ordersList.size == 0) {
            tvOrdersEmpty.visibility = View.VISIBLE
            rvOrdersList.visibility = View.GONE
        }
        else {
            tvOrdersEmpty.visibility = View.GONE
            rvOrdersList.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        uid = sharedViewModel.uid.value!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = DB(requireContext())
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvOrdersEmpty = view.findViewById(R.id.tv_orders_empty)
        rvOrdersList = view.findViewById(R.id.rv_my_orders_list)
        ordersList = db.getOrders(uid)
        ordersEmptyStatus()
        adapter = MyOrdersAdapter(ordersList, this)
        rvOrdersList.adapter = adapter
        rvOrdersList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvOrdersList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.frag_container, SingleOrderFragment.newInstance(ordersList[position].oid))
            addToBackStack(backStackName)
            commit()
        }
    }
}