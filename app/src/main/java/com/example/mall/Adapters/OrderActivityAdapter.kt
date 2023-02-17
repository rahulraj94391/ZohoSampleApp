package com.example.mall.Adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.ModelClass.CartItemModel

class OrderActivityAdapter(private val cartItems: ArrayList<CartItemModel>) : RecyclerView.Adapter<OrderActivityAdapter.OrderActivityViewHolder>() {
    inner class OrderActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderActivityViewHolder {

    }

    override fun onBindViewHolder(holder: OrderActivityViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return cartItems.size + 2
    }

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)


    }
}