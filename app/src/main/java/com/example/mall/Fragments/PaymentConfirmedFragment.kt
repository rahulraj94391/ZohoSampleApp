package com.example.mall.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mall.Adapters.PaymentType
import com.example.mall.R
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentConfirmedFragment(private val paymentType: PaymentType) : Fragment() {
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var cnfPaymentMsg: TextView

    companion object {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            activity?.finish()
        }
    }
}