package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.OrdersModel
import com.example.mall.R
import com.squareup.picasso.Picasso

class MyOrdersAdapter(
    private val ordersList: MutableList<OrdersModel>,
    private val mClickListener: OnClickListener
) : RecyclerView.Adapter<MyOrdersAdapter.OrdersViewHolder>() {
    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prodImage: ImageView = itemView.findViewById(R.id.iv_my_order_img)
        val prodName: TextView = itemView.findViewById(R.id.tv_my_order_name)
        var deliveryStatus: TextView = itemView.findViewById(R.id.tv_my_order_delivery_status)

        init {
            itemView.setOnClickListener {
                mClickListener.onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_my_orders, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        Picasso
            .get()
            .load(ordersList[position].imgURL)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(holder.prodImage)

        holder.deliveryStatus.text = ordersList[position].deliveryStatus
        holder.prodName.text = ordersList[position].prodTitle
    }

    override fun getItemCount(): Int = ordersList.size
}