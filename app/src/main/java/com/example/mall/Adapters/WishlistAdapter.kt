package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.ModelClass.WishlistModel
import com.example.mall.R
import com.squareup.picasso.Picasso

class WishlistAdapter(
    private val wishlistItems: MutableList<WishlistModel>,
    private val listener: WishlistItemClickListener
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.findViewById<Button>(R.id.btn_wishlist_remove_top).setOnClickListener(){
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onTopBtnClicked(adapterPosition)
                }
            }

            itemView.findViewById<Button>(R.id.btn_wishlist_remove_bottom).setOnClickListener(){
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onBottomBtnClicked(adapterPosition)
                }
            }
        }
        var img: ImageView = itemView.findViewById(R.id.iv_wishlist_prod_img)
        var prodName: TextView = itemView.findViewById(R.id.tv_wishlist_product_name)
        var prodPrice: TextView = itemView.findViewById(R.id.tv_wishlist_product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_wishlist, parent, false)
        return WishlistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wishlistItems.size
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        Picasso.get()
            .load(wishlistItems[position].imgURL)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(holder.img)

        holder.prodName.text = wishlistItems[position].prodName
        holder.prodPrice.text = wishlistItems[position].prodPrice.toString()
    }
}

interface WishlistItemClickListener {
    fun onTopBtnClicked(position: Int)
    fun onBottomBtnClicked(position: Int)
}