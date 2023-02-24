package com.example.mall.Adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.R

const val SALES_OFFERS = 1
const val SINGLE_OFFER = 2
const val BACK_IN_STOCK = 3
const val TOP_SELLING = 4

private const val TAG = "Common_Tag_HomeAdapter"


class HomeAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SalesOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class SingleOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class BackInStockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class TopSellingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seeMore: TextView = itemView.findViewById(R.id.see_more)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> SALES_OFFERS
            1, 3 -> SINGLE_OFFER
            4 -> BACK_IN_STOCK
            else -> TOP_SELLING
        }
    }
}