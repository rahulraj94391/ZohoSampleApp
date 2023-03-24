package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnCartItemClickListener
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.R
import com.example.mall.rupeeString
import com.squareup.picasso.Picasso

private const val TAG = "CT_CartItemsAdapter"

class CartItemsAdapter(

    private val cartItems: MutableList<CartItemModel>,
    private val listener: OnCartItemClickListener

) : RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder>() {
    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var minus: Button = itemView.findViewById(R.id.btn_item_decrease)
        var plus: Button = itemView.findViewById(R.id.btn_item_increase)
        var prodPrice: TextView = itemView.findViewById(R.id.tv_prod_price)
        var quantity: TextView = itemView.findViewById(R.id.tv_prod_quantity)
        val imageView: ImageView = itemView.findViewById(R.id.iv_product_image)
        var prodName: TextView = itemView.findViewById(R.id.tv_prod_name)

        init {
            itemView.setOnClickListener { listener.onItemClicked(adapterPosition) }
            plus.setOnClickListener { listener.onQuantityIncrease(plus, adapterPosition, minus) }
            minus.setOnClickListener { listener.onQuantityDecrease(minus, adapterPosition, plus) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsAdapter.CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_cart, parent, false)
        return CartItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: CartItemsAdapter.CartItemViewHolder, position: Int) {
        holder.prodName.text = cartItems[position].productName
        holder.prodPrice.text = rupeeString(cartItems[position].price)
        holder.quantity.text = cartItems[position].quantity.toString()
        listener.changeQtyBtn(holder.plus, position, holder.minus)
        val imageURL = cartItems[position].thumbnailURL
        Picasso.get().load(imageURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(holder.imageView)
    }
}

