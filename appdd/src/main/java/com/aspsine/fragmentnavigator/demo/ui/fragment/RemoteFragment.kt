package com.aspsine.fragmentnavigator.demo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.FragmentNavigator
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.ui.adapter.ChildRemoteFragmentAdapter
import com.aspsine.fragmentnavigator.demo.ui.widget.RemoteTabLayout


/**
 * A simple [Fragment] subclass.
 */
class RemoteFragment : Fragment() {
    private var mNavigator: FragmentNavigator? = null
    private var remoteTabLayout: RemoteTabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavigator =
            FragmentNavigator(childFragmentManager, ChildRemoteFragmentAdapter(), R.id.childContainer)
        mNavigator!!.setDefaultPosition(0)
        mNavigator!!.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remoteTabLayout = view.findViewById<View>(R.id.tabLayoutRemote) as RemoteTabLayout
        remoteTabLayout!!.setOnTabItemClickListener(object : RemoteTabLayout.OnTabItemClickListener{
            override fun onTabItemClick(position: Int, view: View?) {
                setCurrentTab(position)
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCurrentTab(mNavigator!!.currentPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mNavigator!!.onSaveInstanceState(outState)
    }

    fun setCurrentTab(position: Int) {
        mNavigator!!.showFragment(position)
        remoteTabLayout!!.select(position)
    }

    companion object {
        val TAG = RemoteFragment::class.java.simpleName
        fun newInstance(position: Int): Fragment {
            return RemoteFragment()
        }
    }
}