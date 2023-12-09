package com.aspsine.fragmentnavigator.demo.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.aspsine.fragmentnavigator.demo.R

/**
 * Created by aspsine on 16/4/1.
 */
class RemoteTabLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mOnTabItemClickListener: OnTabItemClickListener? = null

    interface OnTabItemClickListener {
        fun onTabItemClick(position: Int, view: View?)
    }

    fun setOnTabItemClickListener(listener: OnTabItemClickListener?) {
        mOnTabItemClickListener = listener
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
        }
    }

    init {
        orientation = HORIZONTAL
        inflate(context, R.layout.layout_tab_remote, this)
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.setOnClickListener { v -> mOnTabItemClickListener!!.onTabItemClick(i, v) }
        }
    }
}