package com.example.mall.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mall.Interface.OnAddressRowClicked
import com.example.mall.ModelClass.DeliveryAddressModel
import com.example.mall.R

const val USER_ADDRESS_ROW: Int = 0
const val ADD_NEW_ADDRESS: Int = 1

private const val TAG = "CT_AddressAdapter"

class AddressAdapter(
    private val addresses: MutableList<DeliveryAddressModel>,
    private val listener: OnAddressRowClicked
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class SavedAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val more: ImageView = itemView.findViewById(R.id.moreDots)
        val deliveryName: TextView = itemView.findViewById(R.id.name)
        val deliveryPhone: TextView = itemView.findViewById(R.id.phone)
        val deliveryAddress: TextView = itemView.findViewById(R.id.address)
        val deliveryPinCode: TextView = itemView.findViewById(R.id.pinCode)

        init {
            more.setOnClickListener() {
                listener.showPopup(adapterPosition, it)
            }
        }
    }

    inner class AddNewAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }

        }
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

    override fun getItemCount(): Int = addresses.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            addresses.size -> ADD_NEW_ADDRESS
            else -> USER_ADDRESS_ROW
        }
    }
}