package com.aspsine.fragmentnavigator.demo.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.utils.SharedPrefUtils
import java.lang.ref.WeakReference

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {
    private val mRunnable = WeakRunnable(this)
    private var mText: String? = null
    private var tvText: TextView? = null
    private var progressBar: ProgressBar? = null
    private val MOCK_LOAD_DATA_DELAYED_TIME : Long = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mText = requireArguments().getString(EXTRA_TEXT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvText = view.findViewById<View>(R.id.tvText) as TextView
        progressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            loadData()
        } else {
            mText = savedInstanceState.getString(EXTRA_TEXT)
            bindData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TEXT, mText)
    }

    override fun onDestroyView() {
        sHandler.removeCallbacks(mRunnable)
        tvText = null
        progressBar = null
        super.onDestroyView()
    }

    private fun showProgressBar(show: Boolean) {
        progressBar!!.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun bindData() {
        val isLogin = SharedPrefUtils.isLogin(activity)
        tvText!!.text = "$mText\nLogin:$isLogin"
    }

    /**
     * mock load data
     */
    private fun loadData() {
        showProgressBar(true)
        //直接加载
        sHandler.post(mRunnable)
        //首次进去延迟加载刷新
//        sHandler.postDelayed(mRunnable, MOCK_LOAD_DATA_DELAYED_TIME);
    }

    private class WeakRunnable(mainFragment: MainFragment) : Runnable {
        var mMainFragmentReference: WeakReference<MainFragment>
        override fun run() {
            val mainFragment = mMainFragmentReference.get()
            if (mainFragment != null && !mainFragment.isDetached) {
                mainFragment.showProgressBar(false)
                mainFragment.bindData()
            }
        }

        init {
            mMainFragmentReference = WeakReference(mainFragment)
        }
    }

    companion object {
        val TAG = MainFragment::class.java.simpleName
        const val EXTRA_TEXT = "extra_text"

        private val sHandler = Handler(Looper.getMainLooper())
        fun newInstance(text: String?): Fragment {
            val fragment = MainFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_TEXT, text)
            fragment.arguments = bundle
            return fragment
        }
    }
}