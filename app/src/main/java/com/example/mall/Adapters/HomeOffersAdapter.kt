package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnClickListener
import com.example.mall.R

class HomeOffersAdapter(
    private val offerImg: MutableList<Int>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<HomeOffersAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val offerImagePlaceHolder: ImageView = itemView.findViewById(R.id.ivImage)

        init {
            offerImagePlaceHolder.setOnClickListener() {
                listener.onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_home_view_pager_image_holder, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.offerImagePlaceHolder.setImageResource(offerImg[position])
    }
}