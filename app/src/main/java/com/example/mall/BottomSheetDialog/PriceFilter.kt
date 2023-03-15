package com.example.mall.BottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mall.Fragments.ProductsListViewFragment
import com.example.mall.R
import com.example.mall.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MIN_RANGE: Int = 0
private const val MAX_RANGE: Int = 999999999


class PriceFilter(
    /*private val selectChipColor: () -> Unit,
    private val deselectChipColor: () -> Unit,
    private val applyPriceFilter: () -> Unit,
    private val clearPriceFilter: () -> Unit*/
) : BottomSheetDialogFragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var tvMinPrice: TextView
    private lateinit var tvMaxPrice: TextView
    private lateinit var btnApply: Button
    private lateinit var btnClear: Button
    private var minRange: Int = MIN_RANGE
    private var maxRange: Int = MAX_RANGE

//    private lateinit var changeChipColor: () -> Unit

    companion object {
        fun newInstance(changeChipColor: () -> Unit) = PriceFilter().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return inflater.inflate(R.layout.fragment_dialog_price_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnApply = view.findViewById(R.id.btn_apply)
        btnClear = view.findViewById(R.id.btn_clear)
        tvMinPrice = view.findViewById(R.id.et_min_price)
        tvMaxPrice = view.findViewById(R.id.et_max_price)

        btnClear.setOnClickListener {
            sharedViewModel.priceRange = Pair(MIN_RANGE, MAX_RANGE)
            tvMinPrice.text = ""
            tvMaxPrice.text = ""
//            deselectChipColor.invoke()
//            clearPriceFilter.invoke()

            val frag = requireActivity().supportFragmentManager.findFragmentByTag("ProductsListViewFragment") as ProductsListViewFragment
            frag.deselectStateChipBackgroundColor(frag.chipPrice)
            frag.removePriceFilter()
            dismiss()
        }

        btnApply.setOnClickListener {
            if (validation()) {
                sharedViewModel.priceRange = Pair(minRange, maxRange)

//                selectChipColor.invoke()
//                applyPriceFilter.invoke()
                val frag = requireActivity().supportFragmentManager.findFragmentByTag("ProductsListViewFragment") as ProductsListViewFragment
                frag.selectStateChipBackgroundColor(frag.chipPrice)
                frag.applyPriceFilters()
                dismiss()
            }
        }
    }

    private fun validation(): Boolean {
        val minPrice = tvMinPrice.text.toString()
        val maxPrice = tvMaxPrice.text.toString()

        return if (minPrice.isNotBlank() && maxPrice.isNotBlank() && minPrice.toInt() < maxPrice.toInt()) {
            minRange = minPrice.toInt()
            maxRange = maxPrice.toInt()
            true
        }
        else {
            Toast.makeText(requireContext(), "Invalid Inputs", Toast.LENGTH_SHORT).show()
            false
        }
    }
}