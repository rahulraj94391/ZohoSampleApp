package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.CheckoutDescriptionListener
import com.example.mall.ModelClass.CartItemModel
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.ModelClass.PriceDetailsModel
import com.example.mall.R
import com.squareup.picasso.Picasso

const val ADDRESS_ROW: Int = 0
const val CART_ROW: Int = 1
const val PRICE_DETAIL_ROW = 2
const val PAYMENT_ROW: Int = 3
private const val TAG = "Common_Tag_OrderActivityAdapter"

class OrderActivityAdapter(
    private val cartItems: ArrayList<CartItemModel>,
    private val checkoutPriceDetails: PriceDetailsModel,
    private val deliveryAddressModel: DeliveryAddressModel,
    private val listener: CheckoutDescriptionListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<Button>(R.id.btn_change_delivery_address).setOnClickListener() {
                listener.changeDeliveryAddress()
            }
        }

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
        init {
            itemView.findViewById<Button>(R.id.btn_proceed_to_payment).setOnClickListener() {
                listener.redirectToPaymentPortal(
                    itemView.findViewById<RadioGroup>(R.id.rg_payment_selector).checkedRadioButtonId
                )
            }
        }
    }

    inner class PriceDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemCount: TextView = itemView.findViewById(R.id.tv_price_x_items)
        val priceBeforeDiscount: TextView = itemView.findViewById(R.id.tv_price_before_discount)
        val discount: TextView = itemView.findViewById(R.id.tv_discount)
        val finalTotalAmount: TextView = itemView.findViewById(R.id.tv_total_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADDRESS_ROW -> AddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_address_confirmation, parent, false))
            PAYMENT_ROW -> PaymentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_payment_confirmation, parent, false))
            PRICE_DETAIL_ROW -> PriceDetailsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_checkout_price_details, parent, false))
            else -> CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_cart_items_checkout_page, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ADDRESS_ROW -> {
                (holder as AddressViewHolder).apply {
                    deliverToPersonName.text = deliveryAddressModel.fullName
                    deliveryAddress.text = deliveryAddressModel.address
                    deliveryPhNumber.text = deliveryAddressModel.mobile
                }
            }

            PAYMENT_ROW -> {
                (holder as PaymentViewHolder).apply {}
            }

            PRICE_DETAIL_ROW -> {
                (holder as PriceDetailsViewHolder).apply {
                    itemCount.text = "Price (" + checkoutPriceDetails.totalItemCount.toString() + " items)"
                    priceBeforeDiscount.text = "₹ " + checkoutPriceDetails.priceBeforeDiscount.toString()
                    discount.text = "₹ " + checkoutPriceDetails.discount.toString()
                    finalTotalAmount.text = "₹ " + checkoutPriceDetails.totalPriceToPay.toString()
                }
            }

            else -> {
                (holder as CartViewHolder).apply {
                    prodName.text = cartItems[position - 1].productName
                    prodPrice.text = "₹ " + cartItems[position - 1].price.toString()
                    prodQuantity.text = "Qty: " + cartItems[position - 1].quantity.toString()
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
        return cartItems.size + 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ADDRESS_ROW
            cartItems.size + 1 -> PRICE_DETAIL_ROW
            cartItems.size + 2 -> PAYMENT_ROW
            else -> CART_ROW
        }
    }
}



