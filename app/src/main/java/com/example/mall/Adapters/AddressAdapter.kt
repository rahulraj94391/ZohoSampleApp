package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnClickListener
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.R

const val USER_ADDRESS_ROW: Int = 0
const val ADD_NEW_ADDRESS: Int = 1

class AddressAdapter(
    private val addresses: MutableList<DeliveryAddressModel>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class SavedAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deliveryName: TextView = itemView.findViewById(R.id.tv_address_name)
        val deliveryPhone: TextView = itemView.findViewById(R.id.tv_address_phone)
        val deliveryAddress: TextView = itemView.findViewById(R.id.tv_address_delivery_address)
        val deliveryPinCode: TextView = itemView.findViewById(R.id.tv_address_PIN_address)
    }

    inner class AddNewAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }
//        val addNewAddress: ImageView = itemView.findViewById(R.id.iv_add_new_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADD_NEW_ADDRESS -> AddNewAddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_add_new_user_address, parent, false))
            else -> SavedAddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_saved_address, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ADD_NEW_ADDRESS -> {
                (holder as AddNewAddressViewHolder)
            }

            else -> {
                (holder as SavedAddressViewHolder).apply {
                    addresses[position].apply {
                        deliveryName.text = fullName
                        deliveryPhone.text = mobile
                        deliveryAddress.text = address
                        deliveryPinCode.text = pinCode
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return addresses.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            addresses.size -> ADD_NEW_ADDRESS
            else -> USER_ADDRESS_ROW
        }
    }
}