package com.example.mall.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.ModelClass.ProductHighlightsModel
import com.example.mall.R

class HighlightsAdapter(
    private val specification: MutableList<ProductHighlightsModel>
) : RecyclerView.Adapter<HighlightsAdapter.HighlightsViewHolder>() {
    inner class HighlightsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val key: TextView = item.findViewById(R.id.highlight_key)
        val value: TextView = item.findViewById(R.id.highlight_val)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightsViewHolder {
        return HighlightsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_highlights, parent, false))
    }

    override fun getItemCount(): Int = specification.size

    override fun onBindViewHolder(holder: HighlightsViewHolder, position: Int) {
        holder.apply {
            key.text = specification[position].key
            value.text = specification[position].value
            if (position % 2 == 0) {
                itemView.setBackgroundColor(Color.parseColor("#F4F4F4"))
            }
            else {
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
    }
}
