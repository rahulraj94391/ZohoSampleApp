package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Adapters.ContactUsAdapter
import com.example.mall.Constants
import com.example.mall.MainActivity
import com.example.mall.R
import com.example.mall.ToolbarTitle

class ContactUsFragment : Fragment() {
    private lateinit var rvContactUs: RecyclerView
    private lateinit var adapter: ContactUsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = ToolbarTitle.CONTACT_US
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvContactUs = view.findViewById(R.id.contact_us_recycler_view)
        adapter = ContactUsAdapter(Constants.contactUsList)
        rvContactUs.adapter = adapter
        rvContactUs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}