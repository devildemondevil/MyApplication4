package com.aspsine.fragmentnavigator.demo.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.FragmentNavigator
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.ui.adapter.ChildRemoteFragmentAdapter
import com.aspsine.fragmentnavigator.demo.ui.widget.LocalTabLayout
import kotlinx.android.synthetic.main.fragment_local.view.*
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 */
class LocalFragment : Fragment() {

    var tagApp = "FragmentNavigator"

    lateinit var tvNvNetWorking : TextView
    lateinit var tvNvAddressing :TextView
    lateinit var tvNvRfInterfacing :TextView
    lateinit var tvNvSecurity :TextView

    lateinit var tvNvSerialInterfacing :TextView
    lateinit var tvNvSleepMode :TextView
    lateinit var tvNvIoSetting :TextView
    lateinit var tvNvDiagonsticCmd :TextView


    lateinit var btnPlusAll :Button
    lateinit var btnMinusAll :Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNvNetWorking = view.findViewById(R.id.local_nv_networking)
        tvNvNetWorking.setOnClickListener {

        }

        tvNvAddressing = view.findViewById(R.id.local_nv_addressing)
        tvNvAddressing.setOnClickListener {

        }

        tvNvRfInterfacing = view.findViewById(R.id.local_nv_rf_interfacing)
        tvNvRfInterfacing.setOnClickListener {

        }

        tvNvSecurity = view.findViewById(R.id.local_nv_security)
        tvNvSecurity.setOnClickListener {

        }

        tvNvSerialInterfacing = view.findViewById(R.id.local_nv_serial_interfacing)
        tvNvSerialInterfacing.setOnClickListener {

        }

        tvNvSleepMode = view.findViewById(R.id.local_nv_sleep_modes)
        tvNvSleepMode.setOnClickListener {

        }

        tvNvIoSetting = view.findViewById(R.id.local_nv_io_settings)
        tvNvIoSetting.setOnClickListener {

        }

        tvNvDiagonsticCmd = view.findViewById(R.id.local_nv_diagnostic_commands)
        tvNvDiagonsticCmd.setOnClickListener {

        }

        btnPlusAll = view.findViewById(R.id.local_nv_btn_plus)
        btnPlusAll.setOnClickListener {

        }

        btnMinusAll = view.findViewById(R.id.local_nv_btn_minus)
        btnMinusAll.setOnClickListener {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    companion object {
        val TAG = LocalFragment::class.java.simpleName
        fun newInstance(position: Int): Fragment {
            return LocalFragment()
        }
    }
}