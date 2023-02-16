package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Fragments.Category
import com.example.mall.ModelClass.AllCategoryModel
import com.example.mall.R

class AllCategoriesAdapter(
    private val categories: MutableList<AllCategoryModel>,
    private val listener: OnItemClickListener
                          ) : RecyclerView.Adapter<AllCategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_categories_row, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.image.setImageResource(categories[position].img)
        holder.category.text = categories[position].category.type()
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener() {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(categories[adapterPosition].category)
                }
            }
        }

        var image: ImageView = itemView.findViewById(R.id.iv_category_image)
        var category: TextView = itemView.findViewById(R.id.tv_category_type)
    }
}


interface OnItemClickListener {
    fun onItemClick(category: Category)
}
