package com.example.mall.Adapters

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mall.Interface.HomeItemClickListeners
import com.example.mall.ModelClass.ItemImgNamePriceModel
import com.example.mall.R
import com.example.mall.rupeeString
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Runnable
import me.relex.circleindicator.CircleIndicator3

const val SALES_OFFERS = 1
const val SINGLE_OFFER = 2
const val BACK_IN_STOCK = 3
const val TOP_SELLING = 4

private const val TAG = "Common_Tag_HomeAdapter"

class HomeAdapter(
    private val offersAdapter: HomeOffersAdapter,
    private val backInStock: MutableList<ItemImgNamePriceModel>,
    private val topSelling: MutableList<ItemImgNamePriceModel>,
    private val listener: HomeItemClickListeners

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val handler: Handler = Handler()

    inner class SalesOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewPage2: ViewPager2 = itemView.findViewById(R.id.vp_home_offers)
        val circleIndicator3: CircleIndicator3 = itemView.findViewById(R.id.home_circle_indicator_3)
    }

    inner class SingleOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.home_single_offer_1)

        init {
            image.setOnClickListener { listener.singleOfferBannerClicked() }
        }
    }

    inner class BackInStockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card1: MaterialCardView = itemView.findViewById(R.id.back_in_stock_1)
        val img1: ImageView = card1.findViewById(R.id.iv_wishlist_prod_img)
        val prodName1: TextView = card1.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice1: TextView = card1.findViewById(R.id.tv_wishlist_product_price)

        private val card2: MaterialCardView = itemView.findViewById(R.id.back_in_stock_2)
        val img2: ImageView = card2.findViewById(R.id.iv_wishlist_prod_img)
        val prodName2: TextView = card2.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice2: TextView = card2.findViewById(R.id.tv_wishlist_product_price)

        private val card3: MaterialCardView = itemView.findViewById(R.id.back_in_stock_3)
        val img3: ImageView = card3.findViewById(R.id.iv_wishlist_prod_img)
        val prodName3: TextView = card3.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice3: TextView = card3.findViewById(R.id.tv_wishlist_product_price)

        private val card4: MaterialCardView = itemView.findViewById(R.id.back_in_stock_4)
        val img4: ImageView = card4.findViewById(R.id.iv_wishlist_prod_img)
        val prodName4: TextView = card4.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice4: TextView = card4.findViewById(R.id.tv_wishlist_product_price)

        init {
            card1.setOnClickListener { listener.backInStockCardClicked(0) }
            card2.setOnClickListener { listener.backInStockCardClicked(1) }
            card3.setOnClickListener { listener.backInStockCardClicked(2) }
            card4.setOnClickListener { listener.backInStockCardClicked(3) }
        }
    }

    inner class TopSellingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card1: MaterialCardView = itemView.findViewById(R.id.top_selling_1)
        val img1: ImageView = card1.findViewById(R.id.iv_wishlist_prod_img)
        val prodName1: TextView = card1.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice1: TextView = card1.findViewById(R.id.tv_wishlist_product_price)

        private val card2: MaterialCardView = itemView.findViewById(R.id.top_selling_2)
        val img2: ImageView = card2.findViewById(R.id.iv_wishlist_prod_img)
        val prodName2: TextView = card2.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice2: TextView = card2.findViewById(R.id.tv_wishlist_product_price)

        private val card3: MaterialCardView = itemView.findViewById(R.id.top_selling_3)
        val img3: ImageView = card3.findViewById(R.id.iv_wishlist_prod_img)
        val prodName3: TextView = card3.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice3: TextView = card3.findViewById(R.id.tv_wishlist_product_price)

        private val card4: MaterialCardView = itemView.findViewById(R.id.top_selling_4)
        val img4: ImageView = card4.findViewById(R.id.iv_wishlist_prod_img)
        val prodName4: TextView = card4.findViewById(R.id.tv_wishlist_product_name)
        val prodPrice4: TextView = card4.findViewById(R.id.tv_wishlist_product_price)


        init {
            card1.setOnClickListener { listener.topSellingCardClicked(0) }
            card2.setOnClickListener { listener.topSellingCardClicked(1) }
            card3.setOnClickListener { listener.topSellingCardClicked(2) }
            card4.setOnClickListener { listener.topSellingCardClicked(3) }
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

    override fun getItemCount() = 4

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            SALES_OFFERS -> {
                (holder as SalesOfferViewHolder).apply {
                    viewPage2.adapter = offersAdapter
                    viewPage2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            Log.d(TAG, "page position of OFFERS = $position")
                            super.onPageSelected(position)
                            handler.removeCallbacksAndMessages(null)
                            handler.postDelayed(
                                Runnable {
                                    if (viewPage2.currentItem == 2) viewPage2.currentItem = 0
                                    else viewPage2.currentItem = viewPage2.currentItem + 1
                                }, 5000
                            )
                        }
                    })
                    circleIndicator3.setViewPager(viewPage2)
                }
            }

            SINGLE_OFFER -> {
                (holder as SingleOfferViewHolder).apply {

                }
            }

            BACK_IN_STOCK -> {
                (holder as BackInStockViewHolder).apply {
                    Picasso.get().load(backInStock[0].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img1)
                    prodName1.text = backInStock[0].prodName
                    prodPrice1.text = String().rupeeString(backInStock[0].prodPrice)

                    Picasso.get().load(backInStock[1].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img2)
                    prodName2.text = backInStock[1].prodName
                    prodPrice2.text = String().rupeeString(backInStock[1].prodPrice)

                    Picasso.get().load(backInStock[2].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img3)
                    prodName3.text = backInStock[2].prodName
                    prodPrice3.text = String().rupeeString(backInStock[2].prodPrice)

                    Picasso.get().load(backInStock[3].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img4)
                    prodName4.text = backInStock[3].prodName
                    prodPrice4.text = String().rupeeString(backInStock[3].prodPrice)
                }
            }

            else -> {
                (holder as TopSellingViewHolder).apply {
                    Picasso.get().load(topSelling[0].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img1)
                    prodName1.text = topSelling[0].prodName
                    prodPrice1.text = String().rupeeString(topSelling[0].prodPrice)

                    Picasso.get().load(topSelling[1].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img2)
                    prodName2.text = topSelling[1].prodName
                    prodPrice2.text = String().rupeeString(topSelling[1].prodPrice)

                    Picasso.get().load(topSelling[2].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img3)
                    prodName3.text = topSelling[2].prodName
                    prodPrice3.text = String().rupeeString(topSelling[2].prodPrice)

                    Picasso.get().load(topSelling[3].imgURL).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(img4)
                    prodName4.text = topSelling[3].prodName
                    prodPrice4.text = String().rupeeString(topSelling[3].prodPrice)

                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> SALES_OFFERS
            1 -> SINGLE_OFFER
            2 -> BACK_IN_STOCK
            else -> TOP_SELLING
        }
    }
}