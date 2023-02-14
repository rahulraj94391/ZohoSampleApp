package com.example.mall.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.*
import com.example.mall.Fragments.ProductDescriptionFragment
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.ModelClass.ProdDescPageModel
import com.squareup.picasso.Picasso

private const val TAG = "Common_Tag_CartItemsAdapter"

class CartItemsAdapter(private val cartItems: MutableList<CartItemModel>) : RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder>() {
    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var prodPrice: TextView = itemView.findViewById(R.id.tv_prod_price)
        var quantity: TextView = itemView.findViewById(R.id.tv_prod_quantity)
        val imageView: ImageView = itemView.findViewById(R.id.iv_product_image)

        var prodName: TextView = itemView.findViewById<TextView?>(R.id.tv_prod_name).also { it ->
            it.setOnClickListener {
                Log.d(TAG, "Clicked on item = ${cartItems[adapterPosition].productName} , uid = ${cartItems[adapterPosition].productId}")
                val activity = it.context as MainActivity
                activity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, ProductDescriptionFragment(cartItems[adapterPosition].productId))
                    addToBackStack(backStackName)
                    commit()
                }
            }
        }

        val btnDelete: Button = itemView.findViewById<Button?>(R.id.btn_delete).also {
            it.setOnClickListener() {
                Log.d(TAG, "delete clicked - ${cartItems[adapterPosition].productName}")
                val id = it.context
                    .getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
                    .getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)

                val rowsDeleted = DB(it.context).deleteItemFromCart(id, cartItems[adapterPosition].productId)
                if (rowsDeleted > 0){
                    cartItems.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }

        val btnWishlist: Button = itemView.findViewById<Button?>(R.id.btn_add_to_wishlist).also {
            it.setOnClickListener() {
                Log.d(TAG, "wishlisted - ${cartItems[adapterPosition].productName}")
                val id = it.context
                    .getSharedPreferences(MSharedPreferences.NAME, AppCompatActivity.MODE_PRIVATE)
                    .getInt(MSharedPreferences.LOGGED_IN_USER_ID, -1)



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
        return CartItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.prodName.text = cartItems[position].productName
        holder.prodPrice.text = "₹ ${cartItems[position].price}"
        holder.quantity.text = "Quantity - ${cartItems[position].quantity}"
        val imageURL = cartItems[position].thumbnailURL
        Picasso.get()
            .load(imageURL)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(holder.imageView)
    }
}