package com.aspsine.fragmentnavigator.demo.ui.widget

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.aspsine.fragmentnavigator.demo.R

/**
 * Created by aspsine on 16/3/31.
 */
class BottomNavigatorView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    var mOnBottomNavigatorViewItemClickListener: OnBottomNavigatorViewItemClickListener? = null

    interface OnBottomNavigatorViewItemClickListener {
        fun onBottomNavigatorViewItemClick(position: Int, view: View?)
    }

    fun select(position: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == position) {
                selectChild(child, true)
            } else {
                selectChild(child, false)
            }
        }
    }

    private fun selectChild(child: View, select: Boolean) {
        if (child is ViewGroup) {
            val group = child
            group.isSelected = select
            for (i in 0 until group.childCount) {
                selectChild(group.getChildAt(i), select)
            }
        } else {
            child.isSelected = select
            if (child is ImageView) {
                val drawable = child.drawable.mutate()
                if (select) {
                    drawable.setColorFilter(
                        resources.getColor(R.color.colorTabSelected),
                        PorterDuff.Mode.SRC_ATOP
                    )
                } else {
                    drawable.setColorFilter(
                        resources.getColor(R.color.colorTabNormal),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            }
        }
    }

    fun setOnBottomNavigatorViewItemClickListener(listener: OnBottomNavigatorViewItemClickListener?) {
        mOnBottomNavigatorViewItemClickListener = listener
    }

    init {
        orientation = HORIZONTAL
        var countChild = childCount
        inflate(context, R.layout.layout_bottom_navigator, this)
        countChild = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.setOnClickListener { v ->
                mOnBottomNavigatorViewItemClickListener!!.onBottomNavigatorViewItemClick(
                    i,
                    v
                )
            }
        }
    }
}