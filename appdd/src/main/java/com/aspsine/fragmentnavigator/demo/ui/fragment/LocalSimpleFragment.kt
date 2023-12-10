package com.aspsine.fragmentnavigator.demo.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.eventbus.MessageZigBeeFrameEvent
import com.aspsine.fragmentnavigator.demo.protocols.ApiFrameType
import com.aspsine.fragmentnavigator.demo.protocols.ProtocolHandler
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdAndQpPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdRespPacket
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.experimental.and

/**
 * A simple [Fragment] subclass.
 * Use the [LocalSimpleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocalSimpleFragment : Fragment() {

    var tagApp = "FragmentNavigator"

    var __protocolHandler: ProtocolHandler = ProtocolHandler()

    lateinit var edtId:EditText
    lateinit var btnIdR:Button
    lateinit var btnIdW:Button

    lateinit var edtSc:EditText
    lateinit var btnScR:Button
    lateinit var btnScW:Button

    lateinit var edtEe:EditText
    lateinit var btnEeR:Button
    lateinit var btnEeW:Button

    lateinit var edtKy:EditText
    lateinit var btnKyR:Button
    lateinit var btnKyW:Button


    var isWrite = false;

    companion object {
        fun newInstance(): Fragment {
            return LocalSimpleFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        edtId = view.findViewById(R.id.et_local_id)
        btnIdR = view.findViewById(R.id.btn_local_id_r)
        btnIdR.setOnClickListener {
            isWrite = false
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameId = 0x01
            packet.atCmd = "ID"
            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"ID:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommand
                ).put(packet));

        }
        btnIdW = view.findViewById(R.id.btn_local_id_w)
        btnIdW.setOnClickListener {
            isWrite = true
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameType = ApiFrameType.API_ATCommandQP.value.toByte()
            packet.frameId = 0x01
            packet.atCmd = "ID"
            if(edtId.text.toString().isEmpty()){
                packet.parasValues = ByteArray(0)
            }else{
                packet.parasValues = hexStringToByte(edtId.text.toString())
            }

            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"ID:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommandQP
                ).put(packet));
        }


        edtSc = view.findViewById(R.id.et_local_sc)
        btnScR = view.findViewById(R.id.btn_local_sc_r)
        btnScR.setOnClickListener {
            isWrite = false
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameId = 0x01
            packet.atCmd = "SC"
            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"SC:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommand
                ).put(packet));

        }
        btnScW = view.findViewById(R.id.btn_local_sc_w)
        btnScW.setOnClickListener {
            isWrite = true
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameType = ApiFrameType.API_ATCommandQP.value.toByte()
            packet.frameId = 0x01
            packet.atCmd = "SC"
            if(edtId.text.toString().isEmpty()){
                packet.parasValues = ByteArray(0)
            }else{
                packet.parasValues = hexStringToByte(edtSc.text.toString())
            }

            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"SC:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommandQP
                ).put(packet));
        }

        edtEe = view.findViewById(R.id.et_local_ee)
        btnEeR = view.findViewById(R.id.btn_local_ee_r)
        btnEeR.setOnClickListener {
            isWrite = false
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameId = 0x01
            packet.atCmd = "EE"
            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"EE:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommand
                ).put(packet));
        }
        btnEeW = view.findViewById(R.id.btn_local_ee_w)
        btnEeW.setOnClickListener {
            isWrite =true
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameType = ApiFrameType.API_ATCommandQP.value.toByte()
            packet.frameId = 0x01
            packet.atCmd = "EE"
            if(edtEe.text.toString().isEmpty()){
                var array : ByteArray = ByteArray(1)
                array[0] = 0x00
                packet.parasValues = array
            }else{
                var array : ByteArray  = ByteArray(1)

                if(edtEe.text.toString().isNotEmpty() && edtEe.text.toString().isNotBlank()){
                    var ee :Int = 0;
                    ee = try {
                        edtEe.text.toString().toInt()
                    }catch (e :NumberFormatException){
                        0x00
                    }
                    if(ee >= 0x01){
                        ee = 0x01
                    }
                    array[0] = ee.toByte()
                }

//                Log.i(tagApp,"EE:${array.toHexString()}")
                packet.parasValues = array
            }

            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"EE:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommandQP
                ).put(packet));
        }

        edtKy = view.findViewById(R.id.et_local_ky)
        btnKyR = view.findViewById(R.id.btn_local_ky_r)
        btnKyR.setOnClickListener {
            isWrite = false
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameId = 0x01
            packet.atCmd = "KY"
            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"KY:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommand
                ).put(packet));
        }
        btnKyW = view.findViewById(R.id.btn_local_ky_w)
        btnKyW.setOnClickListener {
            isWrite = true
            var packet = ApiZigbeeATCmdAndQpPacket()
            packet.frameType = ApiFrameType.API_ATCommandQP.value.toByte()
            packet.frameId = 0x01
            packet.atCmd = "KY"
            if(edtId.text.toString().isEmpty()){
                packet.parasValues = ByteArray(0)
            }else{
                packet.parasValues = hexStringToByte(edtKy.text.toString())
            }

            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//            Log.i(tagApp,"KY:${atcmdArray.toHexString()}")

            EventBus.getDefault().post(
                MessageZigBeeFrameEvent(
                    ApiFrameType.API_ATCommandQP
                ).put(packet));
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onMessageEvent(event: MessageZigBeeFrameEvent?) {
        if (event!!.apiFrameType == ApiFrameType.API_ATResponse) {
            Log.i(tagApp, "LocalSimpleFragment recv MessageZigBeeFrameEvent  API_ATResponse")

            event.getApiAtCmdRespPacket<ApiZigbeeATCmdRespPacket>()?.let {
                Log.i(tagApp,"dealWithApiAtResp  frameType : ${it.frameType.toInt().and(0xFF).toInt().toString(16)}")
                Log.i(tagApp, "dealWithApiAtResp  frameId : ${it.frameId.toInt().and(0xFF).toInt().toString(16)}")
                Log.i(tagApp, "dealWithApiAtResp  atCmd : ${it.atCmd}")
                Log.i(tagApp,"dealWithApiAtResp   cmdStatus : ${it.cmdStatus.toInt().and(0xFF).toInt().toString(16)}")

                Log.i(tagApp, "dealWithApiAtResp  cmdData.size : ${it.cmdData.size}")


                var atCmdHex = cmdStr2Hex(it.atCmd)

                var cmdID = cmdStr2Hex("ID")
                var cmdSC = cmdStr2Hex("SC")
                var cmdEE = cmdStr2Hex("EE")
                var cmdKY = cmdStr2Hex("KY")

                var cmdWR = cmdStr2Hex("WR")
                var cmdAC = cmdStr2Hex("AC")

                if(!isWrite){
                    //it.cmdData.isNotEmpty()
                    if(it.cmdStatus.toInt() != 0x00){
                        //error
                        activity?.let { it1 -> Snackbar.make(it1.findViewById(R.id.fragment_local_simple),"读"+it.atCmd+"错误,code :"+it.cmdStatus.toString(),Snackbar.LENGTH_SHORT).show() }
                        return
                    }
                    activity?.let { it1 -> Snackbar.make(it1.findViewById(R.id.fragment_local_simple),"读"+it.atCmd+"完成",Snackbar.LENGTH_SHORT).show() }
                    when(atCmdHex){
                        cmdID ->{
                            var retConv = bytesToStr(it.cmdData)
                            edtId.text = Editable.Factory.getInstance().newEditable(retConv)

                        }
                        cmdSC ->{
//                            edtSc.text = Editable.Factory.getInstance().newEditable(it.cmdData.toHexString(false))

                        }
                        cmdEE ->{
//                           edtEe.text  = Editable.Factory.getInstance().newEditable(it.cmdData.toHexString())
                        }
                        cmdKY ->{
//                            edtKy.text  = Editable.Factory.getInstance().newEditable(it.cmdData.toHexString(false))
                        }
                        else -> return
                    }
                }else{
                    when(atCmdHex){
                        cmdID,cmdSC,cmdEE,cmdKY ->{


                            if(it.cmdStatus.toInt() != 0x00){
                                activity?.let { it1 -> Snackbar.make(it1.findViewById(R.id.fragment_local_simple),"写"+it.atCmd+"错误,code :"+it.cmdStatus.toString(),Snackbar.LENGTH_SHORT).show() }
                                return
                            }

                            var packet = ApiZigbeeATCmdAndQpPacket()
                            packet.frameType = ApiFrameType.API_ATCommandQP.value.toByte()
                            packet.frameId = 0x01
                            packet.atCmd = "WR"
                            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//                            Log.i(tagApp,"WR:${atcmdArray.toHexString()}")

                            EventBus.getDefault().post(
                                MessageZigBeeFrameEvent(
                                    ApiFrameType.API_ATCommandQP
                                ).put(packet));

                        }

                        cmdWR ->{
                            if(it.cmdStatus.toInt() != 0x00){
                                activity?.let { it1 -> Snackbar.make(it1.findViewById(R.id.fragment_local_simple),"写"+it.atCmd+"错误,code :"+it.cmdStatus.toString(),Snackbar.LENGTH_SHORT).show() }
                                return
                            }

                            var packet = ApiZigbeeATCmdAndQpPacket()
                            packet.frameType = ApiFrameType.API_ATCommandQP.value.toByte()
                            packet.frameId = 0x01
                            packet.atCmd = "AC"
                            var atcmdArray = __protocolHandler.apiZigbeeATCmdPacket.encoding(packet)
//                            Log.i(tagApp,"AC:${atcmdArray.toHexString()}")

                            EventBus.getDefault().post(
                                MessageZigBeeFrameEvent(
                                    ApiFrameType.API_ATCommandQP
                                ).put(packet));

                        }
                        cmdAC ->{
                            if(it.cmdStatus.toInt() != 0x00){
                                activity?.let { it1 -> Snackbar.make(it1.findViewById(R.id.fragment_local_simple),"写错误,code :"+it.cmdStatus.toString(),Snackbar.LENGTH_SHORT).show() }
                                return
                            }else{
                                activity?.let { it1 -> Snackbar.make(it1.findViewById(R.id.fragment_local_simple),"写完成",Snackbar.LENGTH_SHORT).show() }
                            }
                        }
                        else -> return
                    }
                }

            }
        }
    }



    private fun cmdStr2Hex(cmdStr: String) : Int{
        var ret : Int = 0x00;

        if(cmdStr.length > 4)
            return  ret

        for(cmdByte in cmdStr){
            ret =  ret.shl(8).or(cmdByte.toInt().and(0xFF))
        }
        return ret
    }

    private fun bytesToStr(cmdData : ByteArray) : String{
        var ret :String = String()
        for(cmdByte in cmdData){
            ret += cmdByte.toInt().and(0xFF).toString(16);
            if(ret == "00")
                ret = String()
        }

        return ret
    }
    private fun hexStringToByte(hex: String): ByteArray {
        val b = ByteArray(hex.length / 2)
        var j = 0
        for (i in b.indices) {
            val c0 = hex[j++]
            val c1 = hex[j++]
            b[i] = (parse(c0).shl(4) or parse(c1)).toByte()
        }
        return b
    }

    private fun parse(c: Char): Int {
        if (c >= 'a') return c - 'a' + 10 and 0x0f
        return if (c >= 'A') c - 'A' + 10 and 0x0f else c - '0' and 0x0f
    }

}