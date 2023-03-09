package com.example.mall.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.*
import com.example.mall.Adapters.HighlightsAdapter
import com.example.mall.Adapters.ProductDescriptionImagesAdapter
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.ModelClass.ProductHighlightsModel
import com.google.android.material.button.MaterialButtonToggleGroup
import me.relex.circleindicator.CircleIndicator3
import kotlin.properties.Delegates

private const val TAG = "Common_Tag_SingleProductDescriptionFragment"
private const val ARG_PID = "pid"

class SingleProductDescriptionFragment : Fragment() {
    private lateinit var quantityBtn: MaterialButtonToggleGroup
    private lateinit var indicator: CircleIndicator3
    private lateinit var productImages: ViewPager2
    private lateinit var stockIndicator: TextView
    private lateinit var productPrice: TextView
    private var pid: Int by Delegates.notNull()
    private lateinit var productName: TextView
    private lateinit var btnStart: Button
    private lateinit var btnEnd: Button
    private lateinit var db: DB
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var prodHighlights: RecyclerView


    companion object {
        fun newInstance(pid: Int) =
            SingleProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PID, pid)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pid = it.getInt(ARG_PID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
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
        db = DB(requireContext())

        indicator = view.findViewById(R.id.circle_indicator_3)
        productName = view.findViewById(R.id.tv_prod_desc_title)
        productPrice = view.findViewById(R.id.tv_prod_desc_price)
        stockIndicator = view.findViewById(R.id.tv_is_in_stock)
        btnStart = view.findViewById(R.id.btn_at_start)
        btnEnd = view.findViewById(R.id.btn_at_end)
        quantityBtn = view.findViewById(R.id.select_qty_segmented)
        prodHighlights = view.findViewById(R.id.rv_highlights)

        val prodDetails: ProdDescPageModel = db.singleProdDesc(pid)
        val stock = prodDetails.stock
        setupStartBtn()
        setupEndBtn(stock)
        val highlightsList = mutableListOf<ProductHighlightsModel>()
        for ((k, v) in prodDetails.specs) {
            highlightsList.add(ProductHighlightsModel(k, v))
        }
        if (stock > 5) {
            stockIndicator.text = getString(R.string.in_stock)
            stockIndicator.setTextColor(Color.parseColor("#0A9900"))
        }
        else if (stock in 1..5) {
            stockIndicator.text = "Only $stock left in Stock"
            stockIndicator.setTextColor(Color.parseColor("#CD9B00"))
        }
        else if (stock == 0) {
            stockIndicator.text = getString(R.string.out_of_stock)
            stockIndicator.setTextColor(Color.parseColor("#990000"))
        }
        productName.text = prodDetails.name
        productPrice.text = String().rupeeString(prodDetails.price)
        val adapter = HighlightsAdapter(highlightsList)
        prodHighlights.adapter = adapter

        val myLinearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        prodHighlights.layoutManager = myLinearLayoutManager
        productImages = view.findViewById(R.id.vp_prod_desc_images)
        productImages.adapter = ProductDescriptionImagesAdapter(prodDetails.imagesURL)
        indicator.setViewPager(productImages)
    }

    private fun setupEndBtn(stock: Int) {
        if (stock == 0) {
            btnEnd.isEnabled = false
            btnEnd.text = getString(R.string.out_of_stock)
            return
        }

        val goToCartAction = View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                (activity as MainActivity).bottomNavigationView.menu.getItem(3).isChecked = true
                setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                replace(R.id.frag_container, CartFragment())
                addToBackStack(backStackName)
                commit()
            }
        }

        val addToCartAction = View.OnClickListener {
            val quantity = getSelectedQuantity(quantityBtn.checkedButtonId)
            if (quantity != -1 && quantity <= stock) {
                db.addItemToCart(sharedViewModel.uid.value!!, pid, quantity)
                btnEnd.text = getString(R.string.go_to_cart)
                btnEnd.setOnClickListener(goToCartAction)
            }
            else if (quantity != -1 && quantity > stock) {
                Toast.makeText(requireContext(), "Only $stock in stock", Toast.LENGTH_LONG).show()
            }
        }

        if (db.isItemInCart(sharedViewModel.uid.value!!, pid)) {
            btnEnd.text = getString(R.string.go_to_cart)
            btnEnd.setOnClickListener(goToCartAction)
        }
        else {
            btnEnd.text = getString(R.string.add_to_cart)
            btnEnd.setOnClickListener(addToCartAction)
        }
    }

    private fun setupStartBtn() {
        val goToWishlistAction = View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                replace(R.id.frag_container, MyWishlistFragment())
                addToBackStack(backStackName)
                commit()
            }
        }

        val addToWishlistAction = View.OnClickListener {
            db.addItemToWishlist(sharedViewModel.uid.value!!, pid)
            btnStart.text = getString(R.string.go_to_wishlist)
            btnStart.setOnClickListener(goToWishlistAction)
        }

        if (db.isItemInWishlist(sharedViewModel.uid.value!!, pid)) {
            btnStart.text = getString(R.string.go_to_wishlist)
            btnStart.setOnClickListener(goToWishlistAction)
        }
        else {
            btnStart.text = getString(R.string.add_in_wishlist)
            btnStart.setOnClickListener(addToWishlistAction)
        }
    }
}

