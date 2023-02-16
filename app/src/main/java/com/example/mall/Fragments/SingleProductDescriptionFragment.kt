package com.example.mall.Fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.Adapters.ProductDescriptionImagesAdapter
import com.example.mall.DB
import com.example.mall.MSharedPreferences
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.R
import com.example.mall.backStackName
import com.google.android.material.button.MaterialButtonToggleGroup

private const val TAG = "Common_Tag_SingleProductDescriptionFragment"

class SingleProductDescriptionFragment(private val pid: Int, private val statusSwitcher: ChangeBottomNavigationStatus) : Fragment() {
    private lateinit var productImages: ViewPager2
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var quantityBtn: MaterialButtonToggleGroup
    private lateinit var highlights: TextView
    private lateinit var stockIndicator: TextView
    private lateinit var btnStart: Button
    private lateinit var btnEnd: Button
    private lateinit var db: DB
    private var uid: Int = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_description, container, false)
    }

    private fun getSelectedQuantity(id: Int): Int {
        return when (id) {
            R.id.quantity_1 -> 1
            R.id.quantity_2 -> 2
            R.id.quantity_3 -> 3
            R.id.quantity_4 -> 4
            else -> -1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = requireContext().getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE).getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)
        db = DB(requireContext())

        highlights = view.findViewById(R.id.tv_highlights)
        productName = view.findViewById(R.id.tv_prod_desc_title)
        productPrice = view.findViewById(R.id.tv_prod_desc_price)
        stockIndicator = view.findViewById(R.id.tv_is_in_stock)
        btnStart = view.findViewById(R.id.btn_at_start)
        btnEnd = view.findViewById(R.id.btn_at_end)
        quantityBtn = view.findViewById(R.id.select_qty_segmented)

        // get the product details from DB using pid, store in model_class
        val prodDetails: ProdDescPageModel = db.getProductDescription(pid)
        val stock = prodDetails.stock
        setupStartBtn()
        setupEndBtn(stock)
        var specs: String = "" // get the specs from model_class
        for ((k, v) in prodDetails.specs) {
            specs += "$k -> $v\n"
        }

        if (stock > 5) {
            stockIndicator.text = "In Stock"
            stockIndicator.setTextColor(Color.parseColor("#0A9900"))
        }
        else if (stock in 1..5) {
            stockIndicator.text = "Only $stock left in Stock"
            stockIndicator.setTextColor(Color.parseColor("#CD9B00"))
        }
        else if (stock == 0) {
            stockIndicator.text = "Out of Stock"
            stockIndicator.setTextColor(Color.parseColor("#990000"))
        }

        productName.text = prodDetails.name
        productPrice.text = prodDetails.price.toString()
        highlights.text = specs
        productImages = view.findViewById(R.id.vp_prod_desc_images)
        productImages.adapter = ProductDescriptionImagesAdapter(prodDetails.imagesURL)
    }

    private fun setupEndBtn(stock: Int) {
        if (stock == 0) {
            btnEnd.isEnabled = false
            btnEnd.text = "Out of Stock"
            return
        }

        val goToCartAction = View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, CartFragment())
                addToBackStack(backStackName)
                commit()
            }
            statusSwitcher.changeIndicator(3)
        }

        val addToCartAction = View.OnClickListener {
            val quantity = getSelectedQuantity(quantityBtn.checkedButtonId)
            if (quantity != -1 && quantity <= stock) {
                val success = db.addItemToCart(uid, pid, quantity)
                Log.d(TAG, "item added to cart, success = $success")
                btnEnd.text = "Go to Cart"
                btnEnd.setOnClickListener(goToCartAction)
            }
            else if (quantity != -1 && quantity > stock) {
                Toast.makeText(requireContext(), "Only $stock in stock", Toast.LENGTH_LONG).show()
            }
        }

        if (db.isItemInCart(uid, pid)) {
            btnEnd.text = "Go to Cart"
            btnEnd.setOnClickListener(goToCartAction)
        }
        else {
            btnEnd.text = "Add to Cart"
            btnEnd.setOnClickListener(addToCartAction)
        }

    }

    private fun setupStartBtn() {
        val goToWishlistAction = View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, MyWishlistFragment())
                addToBackStack(backStackName)
                commit()
            }
        }

        val addToWishlistAction = View.OnClickListener {
            db.addItemToWishlist(uid, pid)
            btnStart.text = "Go to Wishlist"
            btnStart.setOnClickListener(goToWishlistAction)
        }

        if (db.isItemInWishlist(uid, pid)) {
            btnStart.text = "Go to Wishlist"
            btnStart.setOnClickListener(goToWishlistAction)
        }
        else {
            btnStart.text = "Add in Wishlist"
            btnStart.setOnClickListener(addToWishlistAction)
        }
    }
}

interface ChangeBottomNavigationStatus{
    fun changeIndicator(position: Int)
}

