package com.example.mall

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CT_WishlistItemDecor"


class WishlistItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)


        // apply the left and top margin when item adapter
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = space
            outRect.top = space
        }
        else {
            outRect.right = space
            outRect.left = space
            outRect.top = space
        }

        val itemCount = parent.adapter!!.itemCount

        if (itemCount % 2 == 0) {

            // when the list is even, then apply the margin to the bottom of only the last 2 elements.
            if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1
                || parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 2
            ) {
                outRect.bottom = space
            }
        }
        else {

            //when the list is odd, then apply the margin to the bottom of only the last element.
            if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
                outRect.bottom = space
            }
        }
    }
}