package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mall.MainActivity
import com.example.mall.R
import com.example.mall.ToolbarTitle

class ContactUsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).toolbar.title = ToolbarTitle.CONTACT_US
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }
}