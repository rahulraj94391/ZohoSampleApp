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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        private var deleteBtn: Button = itemView.findViewById(R.id.btn_start)
        private var wishlistBtn: Button = itemView.findViewById(R.id.btn_end)

        init {
            deleteBtn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.deleteItem(adapterPosition)
                }
            }
            wishlistBtn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.wishlistItem(adapterPosition)
                }
            }
            imageView.setOnClickListener { listener.onItemClicked(adapterPosition) }
            plus.setOnClickListener {
                listener.onQuantityIncrease(plus, adapterPosition, minus)
                (it as View).isClickable = false
                CoroutineScope(Dispatchers.Main).launch {
                    delay(130)
                    it.isClickable = true
                }
            }
            minus.setOnClickListener {
                listener.onQuantityDecrease(minus, adapterPosition, plus)
                (it as View).isClickable = false
                CoroutineScope(Dispatchers.Main).launch {
                    delay(130)
                    it.isClickable = true
                }
            }
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

