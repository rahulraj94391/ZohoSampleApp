package com.example.mall.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.Adapters.ProductDescriptionImagesAdapter
import com.example.mall.DB
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.R
import com.google.android.material.button.MaterialButtonToggleGroup

class SingleProductDescriptionFragment(private val pid: Int) : Fragment() {
    lateinit var productImages: ViewPager2
    lateinit var productName: TextView
    lateinit var productPrice: TextView
    lateinit var quantity: MaterialButtonToggleGroup
    lateinit var highlights: TextView
    lateinit var isInStock: TextView
    lateinit var btnStart: Button
    lateinit var btnEnd: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        highlights = view.findViewById(R.id.tv_highlights)
        productName = view.findViewById(R.id.tv_prod_desc_title)
        productPrice = view.findViewById(R.id.tv_prod_desc_price)
        isInStock = view.findViewById(R.id.tv_is_in_stock)
        btnStart = view.findViewById(R.id.btn_at_start)
        btnEnd = view.findViewById(R.id.btn_at_end)



        val prodDetails: ProdDescPageModel = DB(requireContext()).getProductDescription(pid)
        var specs: String = ""
        for ((k, v) in prodDetails.specs) {
            specs += "$k -> $v \n"
        }
        val stock = prodDetails.stock
        if (stock > 5) {
            isInStock.text = "In Stock"
            isInStock.setTextColor(Color.parseColor("#0A9900"))
        } else if (stock in 1..5) {
            isInStock.text = "Few left in Stock"
            isInStock.setTextColor(Color.parseColor("#CD9B00"))

        } else if (stock == 0) {
            isInStock.text = "Out of Stock"
            isInStock.setTextColor(Color.parseColor("#990000"))
            btnEnd.isEnabled = false
            btnEnd.text = "Out of Stock"
        }




        productName.text = prodDetails.name
        productPrice.text = prodDetails.price.toString()
        highlights.text = specs
        productImages = view.findViewById(R.id.vp_prod_desc_images)
        productImages.adapter = ProductDescriptionImagesAdapter(prodDetails.imagesURL)
    }
}