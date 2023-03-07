package com.example.mall

import com.example.mall.ModelClass.ContactUsModel

object Constants {
    var contactUsList: MutableList<ContactUsModel> = mutableListOf()

    init {
        addDataToModel()
    }


    private fun addDataToModel() {
        contactUsList.add(
            ContactUsModel(
                "I want to track my order",
                "Check order status and call delivery agent"
            )
        )

        contactUsList.add(
            ContactUsModel(
                "I want to manage my order",
                "Cancel, change delivery date & address"
            )
        )

        contactUsList.add(
            ContactUsModel(
                "I want help with return & refund",
                "Manage ad track returns"
            )
        )

        contactUsList.add(
            ContactUsModel(
                "I want help with other issue",
                "Offers, payment, Flipkart Plus & all other issues"
            )
        )

        contactUsList.add(
            ContactUsModel(
                "I want to contact the seller",
                "Resolve issue with seller"
            )
        )
    }

}