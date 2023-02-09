package com.example.mall.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mall.R

class HomeFragment : Fragment() {

    override fun onAttach(context: Context) {
//        if (context is BtnChangeListener) listener = context as BtnChangeListener
//        else throw Exception("$context must implement FragmentBListener.")
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }
}