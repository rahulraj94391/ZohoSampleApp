package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.Adapters.ProductDescriptionImagesAdapter
import com.example.mall.DB
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.R
import com.google.android.material.button.MaterialButtonToggleGroup

class ProductDescriptionFragment(private val pid: Int) : Fragment() {
    lateinit var productImages: ViewPager2
    lateinit var productName: TextView
    lateinit var quantity: MaterialButtonToggleGroup


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productName = view.findViewById(R.id.tv_prod_desc_title)
        val prodDetails: ProdDescPageModel = DB(requireContext()).getProductDescription(pid)
        productName.text = prodDetails.name



        productImages = view.findViewById(R.id.vp_prod_desc_images)
        productImages.adapter = ProductDescriptionImagesAdapter(prodDetails.imagesURL)

    }

}


//data class ProdDescPageModel(
//                      val imagesURL: MutableList<String>,
//                      val pid: Int,
//                      val name: String,
//                      val price: Int,
//                      val stock: Int,
//    val specs: MutableMap<String, String>
//)