package com.example.mall.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mall.Enum.PaymentType
import com.example.mall.MainActivity
import com.example.mall.R
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "Common_Tag_PaymentConfirmedFragment"

class PaymentConfirmedFragment(
    private val paymentType: PaymentType
) : Fragment() {
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var cnfPaymentMsg: TextView

    override fun onDestroyView() {
        (activity as MainActivity).bottomNavigationView.visibility = View.VISIBLE
        (activity as MainActivity).toolbar.visibility = View.VISIBLE
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).bottomNavigationView.visibility = View.GONE
        (activity as MainActivity).toolbar.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_payment_confirmed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.lpi_progress_bar)
        cnfPaymentMsg = view.findViewById(R.id.tv_order_confirmation_msg)
        cnfPaymentMsg.text = "Your Order is confirmed.\nYou paid through ${paymentType.paymentMethod()}"

        CoroutineScope(Dispatchers.Main).launch {
            repeat(100) {
                progressBar.progress += 1
                delay(50)
            }
            requireActivity().supportFragmentManager.apply {
                repeat(backStackEntryCount) { popBackStack() }
            }
            (activity as MainActivity).bottomNavigationView.menu.getItem(0).isChecked = true
        }
    }
}