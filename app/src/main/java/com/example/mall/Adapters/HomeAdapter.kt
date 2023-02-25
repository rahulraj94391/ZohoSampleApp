package com.example.mall.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.HomeItemClickListeners
import com.example.mall.ModelClass.ItemImgNamePriceModel
import com.example.mall.R
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

const val SALES_OFFERS = 1
const val SINGLE_OFFER = 2
const val BACK_IN_STOCK = 3
const val TOP_SELLING = 4

private const val TAG = "Common_Tag_HomeAdapter"


class HomeAdapter(

    private val topSelling: MutableList<ItemImgNamePriceModel>,
    private val backInStock: MutableList<ItemImgNamePriceModel>,
    private val listener: HomeItemClickListeners

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class SalesOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class SingleOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class BackInStockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class TopSellingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card1: MaterialCardView = itemView.findViewById(R.id.top_selling_1)
        val img1: ImageView = card1.findViewById(R.id.iv_wishlist_prod_img)
        val prodName1: TextView = card1.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice1: TextView = card1.findViewById(R.id.tv_wishlist_product_price)

        val card2: MaterialCardView = itemView.findViewById(R.id.top_selling_2)
        val img2: ImageView = card2.findViewById(R.id.iv_wishlist_prod_img)
        val prodName2: TextView = card2.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice2: TextView = card2.findViewById(R.id.tv_wishlist_product_price)

        val card3: MaterialCardView = itemView.findViewById(R.id.top_selling_3)
        val img3: ImageView = card3.findViewById(R.id.iv_wishlist_prod_img)
        val prodName3: TextView = card3.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice3: TextView = card3.findViewById(R.id.tv_wishlist_product_price)

        val card4: MaterialCardView = itemView.findViewById(R.id.top_selling_4)
        val img4: ImageView = card4.findViewById(R.id.iv_wishlist_prod_img)
        val prodName4: TextView = card4.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice4: TextView = card4.findViewById(R.id.tv_wishlist_product_price)


        val seeMore: TextView = itemView.findViewById(R.id.see_more)

        init {
            seeMore.setOnClickListener() {
                Log.d(TAG, "see more clicked of Top Selling")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SALES_OFFERS -> SalesOfferViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_home_sale_offers, parent, false))
            SINGLE_OFFER -> SingleOfferViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_home_single_offer, parent, false))
            BACK_IN_STOCK -> BackInStockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_home_back_in_stock, parent, false))
            else -> TopSellingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_home_top_selling, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            SALES_OFFERS -> {
                (holder as SalesOfferViewHolder).apply {

                }
            }

            SINGLE_OFFER -> {
                (holder as SingleOfferViewHolder).apply {

                }
            }

            BACK_IN_STOCK -> {
                (holder as BackInStockViewHolder).apply {

                }
            }

            else -> {
                (holder as TopSellingViewHolder).apply {
                    Picasso.get().load(topSelling[0].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img1)
                    prodName1.text = topSelling[0].prodName
                    prodPrice1.text = topSelling[0].prodPrice.toString()

                    Picasso.get().load(topSelling[1].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img2)
                    prodName2.text = topSelling[1].prodName
                    prodPrice2.text = topSelling[1].prodPrice.toString()

                    Picasso.get().load(topSelling[2].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img3)
                    prodName3.text = topSelling[2].prodName
                    prodPrice3.text = topSelling[2].prodPrice.toString()

                    Picasso.get().load(topSelling[3].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img4)
                    prodName4.text = topSelling[3].prodName
                    prodPrice4.text = topSelling[3].prodPrice.toString()

                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> SALES_OFFERS
            1, 3 -> SINGLE_OFFER
            4 -> BACK_IN_STOCK
            else -> TOP_SELLING
        }
    }
}