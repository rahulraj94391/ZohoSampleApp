package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.ModelClass.ProductHighlightsModel
import com.example.mall.R

//class HighlightsAdapter(
//    val context: Context,
//    private val specification: MutableList<ProductHighlightsModel>
//) : BaseAdapter() {
//    override fun getCount(): Int {
//        return specification.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return specification[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view = LayoutInflater.from(context).inflate(R.layout.row_highlights, null)
//        val key: TextView = view.findViewById(R.id.highlight_key)
//        val value: TextView = view.findViewById(R.id.highlight_val)
//        key.text = specification[position].key
//        value.text = specification[position].value
//        return view
//    }
//}

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

    override fun getItemCount(): Int {
        return specification.size
    }

    override fun onBindViewHolder(holder: HighlightsViewHolder, position: Int) {
        holder.apply {
            key.text = specification[position].key
            value.text = specification[position].value
        }
    }
}
