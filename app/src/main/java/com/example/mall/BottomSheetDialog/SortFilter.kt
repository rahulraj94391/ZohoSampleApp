package com.example.mall.BottomSheetDialog

import android.content.Context
import android.os.Bundle
import android.util.Log
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

private const val TAG = "CT_SortFilter"

class SortFilter : BottomSheetDialogFragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnApply: Button
    private lateinit var btnClear: Button

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: called")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: called")
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return inflater.inflate(R.layout.fragment_dialog_sort_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: called")
    }


    private fun getCheckItem(): SortBy {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.radio_high_to_low -> SortBy.HIGH_TO_LOW
            R.id.radio_low_to_high -> SortBy.LOW_TO_HIGH
            else -> SortBy.NONE
        }
    }
}