package com.example.mall.BottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mall.Enum.SortBy
import com.example.mall.Fragments.ProductsListViewFragment
import com.example.mall.R
import com.example.mall.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortFilter : BottomSheetDialogFragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnApply: Button
    private lateinit var btnClear: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return inflater.inflate(R.layout.fragment_dialog_sort_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnApply = view.findViewById(R.id.btn_apply)
        btnClear = view.findViewById(R.id.btn_clear)
        radioGroup = view.findViewById(R.id.radio_group_sort)

        btnApply.setOnClickListener {
            sharedViewModel.sortMethod = getCheckItem()
            val frag = requireActivity().supportFragmentManager.findFragmentByTag("ProductsListViewFragment") as ProductsListViewFragment
            frag.selectStateChipBackgroundColor(frag.chipSortBy)
            frag.applySortFilter()
            dismiss()
        }

        btnClear.setOnClickListener {
            sharedViewModel.sortMethod = SortBy.NONE
            radioGroup.clearCheck()
            val frag = requireActivity().supportFragmentManager.findFragmentByTag("ProductsListViewFragment") as ProductsListViewFragment
            frag.deselectStateChipBackgroundColor(frag.chipSortBy)
            frag.removeSortFilter()
            dismiss()
        }
    }

    private fun getCheckItem(): SortBy {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.radio_high_to_low -> SortBy.HIGH_TO_LOW
            R.id.radio_low_to_high -> SortBy.LOW_TO_HIGH
            else -> SortBy.NONE
        }
    }
}