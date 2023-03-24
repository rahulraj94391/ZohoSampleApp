package com.example.mall.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnClickListener

import com.example.mall.ModelClass.ProductListModel
import com.example.mall.R
import com.example.mall.rupeeString
import com.squareup.picasso.Picasso

class ProductListAdapter(
    private val products: MutableList<ProductListModel>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        context = recyclerView.context
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(adapterPosition)
                }
            }
        }

        var prodImage: ImageView = itemView.findViewById(R.id.iv_products_list_res)
        var prodName: TextView = itemView.findViewById(R.id.tv_products_list_name)
        var prodPrice: TextView = itemView.findViewById(R.id.tv_products_list_price)
        var isInStock: TextView = itemView.findViewById(R.id.tv_products_list_stock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_product_list_result, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Picasso.get()
            .load(products[position].imgURL)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(holder.prodImage)
        holder.prodName.text = products[position].prodName
        holder.prodPrice.text = rupeeString(products[position].prodPrice)

        val stock = products[position].stock
        if (stock > 5) {
            holder.isInStock.text = context.getString(R.string.in_stock)
            holder.isInStock.setTextColor(Color.parseColor("#0A9900"))
        }
        else if (stock in 1..5) {
            holder.isInStock.text = context.getString(R.string.order_soon)
            holder.isInStock.setTextColor(Color.parseColor("#CD9B00"))

        }
        else if (stock == 0) {
            holder.isInStock.text = context.getString(R.string.out_of_stock)
            holder.isInStock.setTextColor(Color.parseColor("#990000"))
        }
    }
}

