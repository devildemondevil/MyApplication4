package com.aspsine.fragmentnavigator.demo.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.eventbus.McgfCtEvent
import com.aspsine.fragmentnavigator.demo.protocols.MCFGCtHeaderCmdType
import com.aspsine.fragmentnavigator.demo.protocols.ProtocolHandler
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCtPacket
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgExtendedParaRWPacket
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 * Use the [RemoteExtFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemoteExtFragment : Fragment() {

    var tagApp = "FragmentNavigator"
    var __protocolHandler: ProtocolHandler = ProtocolHandler()

    var rwPacket__ = McfgExtendedParaRWPacket()

    lateinit var fabRead: FloatingActionButton
    lateinit var fabWrite: FloatingActionButton

    lateinit var edtPowerLevel : EditText
    lateinit var spEncryEnable :Spinner
    lateinit var spEncryOpt :Spinner
    lateinit var edtConnKey :EditText
    lateinit var edtNetKey :EditText
    lateinit var edtDestAddr :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remote_ext, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtPowerLevel = view.findViewById(R.id.et_extern_power)

        spEncryEnable = view.findViewById(R.id.sp_extern_encrypt_flag)
        ArrayAdapter.createFromResource(
            activity?.baseContext!!,
            R.array.remote_extern_encrypt_enable_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            spEncryEnable.adapter = adapter
        }

        spEncryOpt = view.findViewById(R.id.sp_extern_encrypt_option)
        ArrayAdapter.createFromResource(
            activity?.baseContext!!,
            R.array.remote_extern_encrypt_opt_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            spEncryOpt.adapter = adapter
        }

        edtConnKey = view .findViewById(R.id.et_extern_link_key)
        edtNetKey = view.findViewById(R.id.et_extern_net_key)
        edtDestAddr = view.findViewById(R.id.et_extern_addr_64)

        fabRead = view.findViewById(R.id.fab_remote_extern_read)
        fabRead.setOnClickListener(View.OnClickListener {
//                v -> Snackbar.make(v, "读 扩展参数", Snackbar.LENGTH_SHORT).show()
            EventBus.getDefault().post(
                McgfCtEvent(
                    McgfCtEvent.RwType.MsgSend,
                MCFGCtHeaderCmdType.ExtendParamRead
            ).put(0));
        })
        fabWrite = view.findViewById(R.id.fab_remote_extern_write)
        fabWrite.setOnClickListener(View.OnClickListener {
//                v -> Snackbar.make(v, "写 扩展参数", Snackbar.LENGTH_SHORT).show()
            var mcfgCtPacket = convertUiDataToPacket()
            EventBus.getDefault().post(
                McgfCtEvent(
                    McgfCtEvent.RwType.MsgSend,
                MCFGCtHeaderCmdType.ExtendParamWrite
            ).put(mcfgCtPacket));

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onMessageEvent(event: McgfCtEvent?) {
        if (event!!.rwType == McgfCtEvent.RwType.MsgRecv) {
            when(event.mcfgCtHeaderCmdType){
                //参数读取
                MCFGCtHeaderCmdType.ExtendParamRead ->{
                    Log.i(tagApp, "McgfCtEvent.RwType.MsgRecv  ExtendParamRead")
                    var mcfgCtPacket = __protocolHandler.mcfgCtPacketHandler.encoding(event.getMcfgCtPacket<McfgCtPacket>())
                    var externParaRWPacket = __protocolHandler.mcfgExtendedParaRWPacketHandler.decoding(mcfgCtPacket)
                    rwPacket__ = externParaRWPacket
                    updateUi(externParaRWPacket)
                }
                //参数设置返回
                MCFGCtHeaderCmdType.ExtendParamWrite -> activity?.let { Snackbar.make(it.findViewById(R.id.fragment_remote_ext),"写扩展参数成功",Snackbar.LENGTH_SHORT).show() }
            }

        }

    }


    private fun convertUiDataToPacket(): McfgCtPacket {

        var packet = rwPacket__

        packet.cmdOpt = MCFGCtHeaderCmdType.ExtendParamWrite.value.toByte()

        packet.encryptEnable = spEncryEnable.selectedItemPosition.toByte()

        return __protocolHandler.mcfgCtPacketHandler.decoding(__protocolHandler.mcfgExtendedParaRWPacketHandler.encoding(packet))
    }



    private fun updateUi(externParaRWPacket: McfgExtendedParaRWPacket) {

        edtPowerLevel.text =  Editable.Factory.getInstance().newEditable(externParaRWPacket.powerLevel.toInt().and(0xFF).toString())

        var encryptEnableIndex = externParaRWPacket.encryptEnable.toInt().and(0xFF)

        if(encryptEnableIndex < resources.getStringArray(R.array.remote_extern_encrypt_enable_array).size){
            spEncryEnable.setSelection(encryptEnableIndex)
        }

        var encryptOptIndex = externParaRWPacket.encryptOpt.toInt().and(0xFF)
        if(encryptOptIndex < resources.getStringArray(R.array.remote_extern_encrypt_opt_array).size){
            spEncryOpt.setSelection(encryptOptIndex)
        }

        activity?.let { Snackbar.make(it.findViewById(R.id.fragment_remote_ext),"读扩展参数成功",Snackbar.LENGTH_SHORT).show() }
//        var connKey = externParaRWPacket.connKey.toHexString()
//        edtConnKey.text = Editable.Factory.getInstance().newEditable(connKey)
//
//
//        var netKey = externParaRWPacket.netKey.toHexString()
//        edtNetKey.text = Editable.Factory.getInstance().newEditable(netKey)
//
//        var destAddr = externParaRWPacket.destAddr.toHexString()
//        edtDestAddr.text = Editable.Factory.getInstance().newEditable(destAddr)

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}