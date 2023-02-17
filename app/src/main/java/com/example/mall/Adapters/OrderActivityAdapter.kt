package com.example.mall.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.R
import com.squareup.picasso.Picasso

const val CART_ROW: Int = 1
const val ADDRESS_ROW: Int = 0
const val PAYMENT_ROW: Int = -1
private const val TAG = "Common_Tag_OrderActivityAdapter"

class OrderActivityAdapter(
    private val cartItems: ArrayList<CartItemModel>


) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deliverToPersonName: TextView = itemView.findViewById(R.id.tv_delivery_to_person_name)
        val deliveryAddress: TextView = itemView.findViewById(R.id.tv_delivery_address)
        val deliveryPhNumber: TextView = itemView.findViewById(R.id.tv_delivery_to_phone)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prodImage: ImageView = itemView.findViewById(R.id.iv_checkout_prod_img)
        val prodName: TextView = itemView.findViewById(R.id.tv_checkout_prod_name)
        val prodPrice: TextView = itemView.findViewById(R.id.tv_checkout_prod_price)
        val prodQuantity: TextView = itemView.findViewById(R.id.tv_checkout_prod_qty)
    }

    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paymentOptions: RadioGroup = itemView.findViewById(R.id.rg_payment_selector)
        val payButton: Button = itemView.findViewById(R.id.btn_proceed_to_payment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADDRESS_ROW -> AddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_address_confirmation, parent, false))
            PAYMENT_ROW -> PaymentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_payment_confirmation, parent, false))
            else -> CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_cart_items_checkout_page, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ADDRESS_ROW -> {

            }

            PAYMENT_ROW -> {

            }

            else -> {
                Log.d(TAG, "POSITION = $position")
                (holder as CartViewHolder).apply {
                    prodName.text = cartItems[position - 1].productName
                    prodPrice.text = cartItems[position - 1].price.toString()
                    prodQuantity.text = cartItems[position - 1].quantity.toString()
                    Picasso.get()
                        .load(cartItems[position - 1].thumbnailURL)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(holder.prodImage)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ADDRESS_ROW
            cartItems.size + 1 -> PAYMENT_ROW
            else -> CART_ROW
        }
    }
}