package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.WishlistItemClickListener
import com.example.mall.ModelClass.ItemImgNamePriceModel
import com.example.mall.R
import com.example.mall.rupeeString
import com.squareup.picasso.Picasso

class WishlistAdapter(
    private val wishlistItems: MutableList<ItemImgNamePriceModel>,
    private val listener: WishlistItemClickListener
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(adapterPosition)
                }
            }

            itemView.findViewById<Button>(R.id.btn_wishlist_remove_top).setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onTopBtnClicked(adapterPosition)
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
        holder.prodPrice.text = rupeeString(wishlistItems[position].prodPrice)
    }
}
