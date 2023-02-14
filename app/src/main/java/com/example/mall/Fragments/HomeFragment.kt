package com.example.mall.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.R
import com.example.mall.Adapters.ProductDescriptionImagesAdapter


class HomeFragment : Fragment() {
    lateinit var viewpager: ViewPager2


    override fun onAttach(context: Context) {
//        if (context is BtnChangeListener) listener = context as BtnChangeListener
//        else throw Exception("$context must implement FragmentBListener.")
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
//        val listURL = mutableListOf(
//            "https://rukminim1.flixcart.com/image/832/832/xif0q/mobile/0/h/k/-original-imagcg22czc3ggvw.jpeg",
//            "https://rukminim1.flixcart.com/image/832/832/xif0q/mobile/b/d/n/-original-imagc829xnfx6mgf.jpeg",
//            "https://rukminim1.flixcart.com/image/832/832/xif0q/mobile/e/l/j/-original-imagc829zajwazfm.jpeg",
//        )
//        viewpager = view.findViewById(R.id.vp_prod_desc_images)
//        viewpager.adapter = ProductDescriptionImagesAdapter(listURL)
        return view
    }
}