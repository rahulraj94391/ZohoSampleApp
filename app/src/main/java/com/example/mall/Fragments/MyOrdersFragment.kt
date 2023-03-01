package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Adapters.MyOrdersAdapter
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.OrdersModel

class MyOrdersFragment : Fragment(), OnClickListener {
    private lateinit var rvOrdersList: RecyclerView
    private lateinit var db: DB
    private lateinit var ordersList: MutableList<OrdersModel>
    private var uid = -1
    private lateinit var adapter: MyOrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = "My Orders"
        db = DB(requireContext())
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvOrdersList = view.findViewById(R.id.rv_my_orders_list)
        ordersList = db.getOrders(uid)
        adapter = MyOrdersAdapter(ordersList, this)
        rvOrdersList.adapter = adapter
        rvOrdersList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvOrdersList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, SingleOrderFragment.newInstance(ordersList[position].oid))
            addToBackStack(backStackName)
            commit()
        }
    }
}