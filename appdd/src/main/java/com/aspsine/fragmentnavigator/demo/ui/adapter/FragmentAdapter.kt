package com.aspsine.fragmentnavigator.demo.ui.adapter

import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter
import com.aspsine.fragmentnavigator.demo.ui.fragment.*

/**
 * Created by aspsine on 16/3/31.
 */
class FragmentAdapter : FragmentNavigatorAdapter {
    override fun onCreateFragment(position: Int): Fragment {
        return when(position){
            0 -> LocalSimpleFragment.Companion.newInstance()
            1 -> RemoteFragment.Companion.newInstance(position)
            else ->{
                MainFragment.Companion.newInstance(TABS[position])
            }
        }
        //加载下方导航  第  X  页
//        if (position == 0) {
////            BasicFragment fragment = new BasicFragment();
//            return BasicFragment()
//        } else if (position == 2) {
//            return ContactsFragment.Companion.newInstance(position)
//        }
//        return MainFragment.Companion.newInstance(TABS[position])
    }

    override fun getTag(position: Int): String {
//        return if (position == 2) {
//            RemoteFragment.Companion.TAG
//        } else MainFragment.Companion.TAG + TABS[position]
        return MainFragment.Companion.TAG + TABS[position]
    }

    override fun getCount(): Int {
        return TABS.size
    }

    companion object {
//        private val TABS = arrayOf("基础设置", "模块参数", "远程参数", "调试信息")
        private val TABS = arrayOf("模块参数", "远程参数")
    }
}