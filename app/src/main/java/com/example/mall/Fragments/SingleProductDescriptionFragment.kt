package com.example.mall.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mall.*
import com.example.mall.Adapters.HighlightsAdapter
import com.example.mall.Adapters.ProductDescriptionImagesAdapter
import com.example.mall.ModelClass.ProdDescPageModel
import com.example.mall.ModelClass.ProductHighlightsModel
import com.example.mall.databinding.FragmentSingleProductDescriptionBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.properties.Delegates


private const val TAG = "CT_SingleProdDescFrag"
private const val ARG_PID = "pid"

class SingleProductDescriptionFragment : Fragment() {
    private var pid: Int by Delegates.notNull()
    private lateinit var db: DB
    private lateinit var binding: FragmentSingleProductDescriptionBinding
    private lateinit var highlightsAdapter: HighlightsAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var arrayAdapter: ArrayAdapter<Int>
    private lateinit var mMenuProvider: MenuProvider
    private lateinit var prodDetails: ProdDescPageModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        db = DB(requireContext())
        prodDetails = db.singleProdDesc(pid)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_product_description, container, false)

        mMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu_share, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.share_product -> {
                        shareAction()
                        return true
                    }
                }
                return false
            }
        }

        (requireActivity() as MenuHost).addMenuProvider(mMenuProvider, viewLifecycleOwner, Lifecycle.State.STARTED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemQuantities: Array<Int> = arrayOf(1, 2, 3, 4)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_spinner, itemQuantities)
        binding.qtySelector.adapter = arrayAdapter
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
        binding.prodPrice.text = rupeeString(prodDetails.price)
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

//        binding.imageToShare.setImageBitmap(getBitmapFromURL())
        Picasso.get().load(prodDetails.imagesURL[0]).into(binding.imageToShare)
    }

    private fun shareAction() {
        val bitmapDrawable: BitmapDrawable = binding.imageToShare.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val textToShare = "Take a look at this ${prodDetails.name} on Shopie\n\nhttps://shopie.com/pid=${prodDetails.pid}"
        val bmpUri: Uri? = saveImage(bitmap)
        share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        share.putExtra(Intent.EXTRA_STREAM, bmpUri)
        share.putExtra(Intent.EXTRA_SUBJECT, "New App")
        share.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(share, "Share: "))
    }

    private fun saveImage(image: Bitmap): Uri? {
        val imagesFolder: File = File(requireActivity().cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file: File = File(imagesFolder, "shared_images.jpg")
            val stream: FileOutputStream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(Objects.requireNonNull(requireActivity().applicationContext), "com.example.mall" + ".provider", file)
        } catch (e: Exception) {
            Log.d(TAG, "Exception: + ${e.message}")
        }
        return uri
    }

    // not working, throws NPE
    private fun getBitmapFromURL(): Bitmap? {
        return try {
            val url = URL(prodDetails.imagesURL[0])
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun setupEndBtn(stock: Int) {
        if (stock == 0) {
            binding.endButton.isEnabled = false
            binding.endButton.text = getString(R.string.out_of_stock)
            return
        }

        val goToCartAction = View.OnClickListener { navigateNextWithCustomAnim(CartFragment(), "CartFragment") }

        val addToCartAction = View.OnClickListener {
            (requireActivity() as MainActivity).haptics.light()
            val quantity = binding.qtySelector.selectedItem.toString().trim().toInt()
            if (quantity != -1 && quantity <= stock) {
                db.addItemToCart(sharedViewModel.uid, pid, quantity)
                binding.endButton.text = getString(R.string.go_to_cart)
                binding.endButton.setOnClickListener(goToCartAction)
                Toast.makeText(requireContext(), "Item added to cart.", Toast.LENGTH_SHORT).show()
            }
            else if (quantity != -1) {
                Toast.makeText(requireContext(), "Only $stock in stock", Toast.LENGTH_LONG).show()
            }
        }

        if (db.isItemInCart(sharedViewModel.uid, pid)) {
            binding.endButton.text = getString(R.string.go_to_cart)
            binding.endButton.setOnClickListener(goToCartAction)
        }
        else {
            binding.endButton.text = getString(R.string.add_to_cart)
            binding.endButton.setOnClickListener(addToCartAction)
        }
    }

    private fun setupStartBtn() {
        val goToWishlistAction = View.OnClickListener { navigateNextWithCustomAnim(MyWishlistFragment(), "MyWishlistFragment") }

        val addToWishlistAction = View.OnClickListener {
            Toast.makeText(requireContext(), "Item added to wishlist.", Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).haptics.light()
            db.addItemToWishlist(sharedViewModel.uid, pid)
            binding.startButton.text = getString(R.string.go_to_wishlist)
            binding.startButton.setOnClickListener(goToWishlistAction)
        }

        if (db.isItemInWishlist(sharedViewModel.uid, pid)) {
            binding.startButton.text = getString(R.string.go_to_wishlist)
            binding.startButton.setOnClickListener(goToWishlistAction)
        }
        else {
            binding.startButton.text = getString(R.string.add_in_wishlist)
            binding.startButton.setOnClickListener(addToWishlistAction)
        }
    }
}

