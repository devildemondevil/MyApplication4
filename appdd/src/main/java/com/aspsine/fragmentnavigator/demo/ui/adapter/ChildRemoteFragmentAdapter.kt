package com.aspsine.fragmentnavigator.demo.ui.adapter

import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter
import com.aspsine.fragmentnavigator.demo.ui.fragment.MainFragment
import com.aspsine.fragmentnavigator.demo.ui.fragment.RemoteBasicFragment
import com.aspsine.fragmentnavigator.demo.ui.fragment.RemoteCalFragment
import com.aspsine.fragmentnavigator.demo.ui.fragment.RemoteExtFragment

/**
 * Created by aspsine on 16/4/3.
 */
class ChildRemoteFragmentAdapter : FragmentNavigatorAdapter {
    override fun onCreateFragment(position: Int): Fragment {
        //加载上方导航 页
//        return MainFragment.newInstance(TABS[position]);
        return when (position) {
            0 -> RemoteBasicFragment()
            1 -> RemoteExtFragment()
            2 -> //                return  MainFragment.newInstance("参数校准");
                RemoteCalFragment()
            else -> RemoteBasicFragment()
        }
    }

    override fun getTag(position: Int): String {
        return MainFragment.Companion.TAG + TABS[position]
    }

    override fun getCount(): Int {
        return TABS.size
    }

    companion object {
        val TABS = arrayOf("Friends", "Groups", "Official")
    }
}