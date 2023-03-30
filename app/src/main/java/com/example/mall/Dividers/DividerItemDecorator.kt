package com.example.mall.Dividers

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CT_DividerItemDecorator"

class DividerItemDecorator(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        super.onDraw(c, parent, state)
        val dividerLeft = parent.paddingLeft + 40
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount

        Log.d(TAG, "CHILD COUNT = $childCount")

        for (i in 0 until childCount) {

            if (i == 0 || i == 3) {
                val child: View = parent.getChildAt(i)
                val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop: Int = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + 30

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                mDivider.draw(c)
            }
            else {
                val child: View = parent.getChildAt(i)
                val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop: Int = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + mDivider.intrinsicHeight

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                mDivider.draw(c)
            }


        }
    }
}