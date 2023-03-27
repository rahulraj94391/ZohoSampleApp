package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.ContactUsAdapter
import com.example.mall.ModelClass.ContactUsModel
import com.example.mall.R

class ContactUsFragment : Fragment() {
    private lateinit var rvContactUs: RecyclerView
    private lateinit var adapter: ContactUsAdapter
    private lateinit var contactUsList: MutableList<ContactUsModel>

    init {
        addOptions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvContactUs = view.findViewById(R.id.contact_us_recycler_view)
        adapter = ContactUsAdapter(contactUsList)
        rvContactUs.adapter = adapter
        rvContactUs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun addOptions() {
        contactUsList.add(ContactUsModel("I want to track my order", "Check order status and call delivery agent"))
        contactUsList.add(ContactUsModel("I want to manage my order", "Cancel, change delivery date & address"))
        contactUsList.add(ContactUsModel("I want help with return & refund", "Manage and track returns"))
        contactUsList.add(ContactUsModel("I want help with other issue", "Offers, payment, Flipkart Plus & all other issues"))
        contactUsList.add(ContactUsModel("I want to contact the seller", "Resolve issue with seller"))
    }
}