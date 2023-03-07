package com.example.mall.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.ModelClass.ContactUsModel
import com.example.mall.R

private const val TITLE = 0
private const val ISSUE = 1

class ContactUsAdapter(private val list: MutableList<ContactUsModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.context = recyclerView.context
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.contact_us_issue_title)
    }

    inner class IssueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val helpTitle: TextView = itemView.findViewById(R.id.help_title)
        val helpDesc: TextView = itemView.findViewById(R.id.help_desc)

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "clicked $adapterPosition", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TITLE -> TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_contact_us_title, parent, false))
            else -> IssueViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_contact_us, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TITLE -> {
                (holder as TitleViewHolder).apply {
                    title.text = "What issue are you facing ?"
                }
            }
            else -> {
                (holder as IssueViewHolder).apply {
                    helpTitle.text = list[position - 1].issue
                    helpDesc.text = list[position - 1].desc
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TITLE
            else -> ISSUE
        }
    }

    override fun getItemCount() = 6
}