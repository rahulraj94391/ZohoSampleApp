package com.example.mall.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mall.*
import com.example.mall.Adapters.HighlightsAdapter
import com.example.mall.Adapters.ProductDescriptionImagesAdapter
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.ModelClass.ProductHighlightsModel
import com.example.mall.databinding.FragmentSingleProductDescriptionBinding
import kotlin.properties.Delegates

private const val TAG = "CT_SingleProdDescFrag"
private const val ARG_PID = "pid"

class SingleProductDescriptionFragment : Fragment() {
    private var pid: Int by Delegates.notNull()
    private lateinit var binding: FragmentSingleProductDescriptionBinding
    private lateinit var highlightsAdapter: HighlightsAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var arrayAdapter: ArrayAdapter<Int>
    private lateinit var db: DB

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_product_description, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DB(requireContext())

        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_spinner, Constants.itemQuantities)
        binding.qtySelector.adapter = arrayAdapter

        val prodDetails: ProdDescPageModel = db.singleProdDesc(pid)
        val stock = prodDetails.stock
        setupStartBtn()
        setupEndBtn(stock)
        val highlightsList = mutableListOf<ProductHighlightsModel>()
        for ((k, v) in prodDetails.specs) {
            highlightsList.add(ProductHighlightsModel(k, v))
        }
        if (stock > 5) {
            with(binding) {
                stockIndicator.text = getString(R.string.in_stock)
                stockIndicator.setTextColor(Color.parseColor("#0A9900"))
            }
        }
        else if (stock in 1..5) {
            binding.stockIndicator.text = "Only $stock left in Stock"
            binding.stockIndicator.setTextColor(Color.parseColor("#CD9B00"))
        }
        else if (stock == 0) {
            binding.stockIndicator.text = getString(R.string.out_of_stock)
            binding.stockIndicator.setTextColor(Color.parseColor("#990000"))
        }
        binding.prodName.text = prodDetails.name
        binding.prodPrice.text = String().rupeeString(prodDetails.price)
        highlightsAdapter = HighlightsAdapter(highlightsList)

        binding.prodHighlights.apply {
            adapter = highlightsAdapter
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
                override fun canScrollHorizontally(): Boolean = false
            }
        }
        binding.prodImages.adapter = ProductDescriptionImagesAdapter(prodDetails.imagesURL)
        binding.viewPagerIndicator.setViewPager(binding.prodImages)
    }

    private fun setupEndBtn(stock: Int) {
        if (stock == 0) {
            binding.endButton.isEnabled = false
            binding.endButton.text = getString(R.string.out_of_stock)
            return
        }

        val goToCartAction = View.OnClickListener { navigateNextWithCustomAnim(CartFragment()) }

        val addToCartAction = View.OnClickListener {
            val quantity = binding.qtySelector.selectedItem.toString().trim().toInt()
            if (quantity != -1 && quantity <= stock) {
                db.addItemToCart(sharedViewModel.uid.value!!, pid, quantity)
                binding.endButton.text = getString(R.string.go_to_cart)
                binding.endButton.setOnClickListener(goToCartAction)
            }
            else if (quantity != -1 && quantity > stock) {
                Toast.makeText(requireContext(), "Only $stock in stock", Toast.LENGTH_LONG).show()
            }
        }

        if (db.isItemInCart(sharedViewModel.uid.value!!, pid)) {
            binding.endButton.text = getString(R.string.go_to_cart)
            binding.endButton.setOnClickListener(goToCartAction)
        }
        else {
            binding.endButton.text = getString(R.string.add_to_cart)
            binding.endButton.setOnClickListener(addToCartAction)
        }
    }

    private fun setupStartBtn() {
        val goToWishlistAction = View.OnClickListener { navigateNextWithCustomAnim(MyWishlistFragment()) }

        val addToWishlistAction = View.OnClickListener {
            db.addItemToWishlist(sharedViewModel.uid.value!!, pid)
            binding.startButton.text = getString(R.string.go_to_wishlist)
            binding.startButton.setOnClickListener(goToWishlistAction)
        }

        if (db.isItemInWishlist(sharedViewModel.uid.value!!, pid)) {
            binding.startButton.text = getString(R.string.go_to_wishlist)
            binding.startButton.setOnClickListener(goToWishlistAction)
        }
        else {
            binding.startButton.text = getString(R.string.add_in_wishlist)
            binding.startButton.setOnClickListener(addToWishlistAction)
        }
    }
}

