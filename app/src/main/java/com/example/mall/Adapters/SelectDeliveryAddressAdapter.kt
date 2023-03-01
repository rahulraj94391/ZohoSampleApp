package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnNewAddressSelected
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.R

class SelectDeliveryAddressAdapter(
    private val addressList: MutableList<DeliveryAddressModel>,
    private val listener: OnNewAddressSelected
) : RecyclerView.Adapter<SelectDeliveryAddressAdapter.AddressViewHolder>() {
    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.selectedNewAddress(adapterPosition)
            }
        }

        val name: TextView = itemView.findViewById(R.id.tv_new_del_name)
        val phoneNumber: TextView = itemView.findViewById(R.id.tv_new_del_mobile)
        val address: TextView = itemView.findViewById(R.id.tv_new_del_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_change_delivery_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.name.text = addressList[position].fullName
        holder.phoneNumber.text = addressList[position].mobile
        holder.address.text = addressList[position].address + ", " + addressList[position].pinCode
    }
}