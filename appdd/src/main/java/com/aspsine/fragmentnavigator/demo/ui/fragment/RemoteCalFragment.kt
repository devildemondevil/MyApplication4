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
import com.aspsine.fragmentnavigator.demo.eventbus.McgfCtEvent
import com.aspsine.fragmentnavigator.demo.protocols.MCFGCtHeaderCmdType
import com.aspsine.fragmentnavigator.demo.protocols.ProtocolHandler
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.*
import com.google.android.material.snackbar.Snackbar
import com.swallowsonny.convertextlibrary.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 */
class RemoteCalFragment : Fragment() {

    var tagApp = "FragmentNavigator"
    var __protocolHandler: ProtocolHandler = ProtocolHandler()

    var rwPacket__ = McfgCalibrateRWPacket()

    lateinit var edtChannelAd:EditText
    lateinit var btnAdcRead :Button
    lateinit var edtAdcF: EditText
    lateinit var edtAdcI :EditText

    lateinit var edtChannelCal :EditText
    lateinit var btnCaliRead :Button
    lateinit var btnCaliWrite :Button

    lateinit var edtSensorNum : EditText
    lateinit var edtCaliPts : EditText

    lateinit var edtPt1Ref :EditText
    lateinit var edtPt1Ad :EditText
    lateinit var btnPt1SetAd :Button

    lateinit var edtPt2Ref :EditText
    lateinit var edtPt2Ad :EditText
    lateinit var btnPt2SetAd :Button

    lateinit var edtPt3Ref :EditText
    lateinit var edtPt3Ad :EditText
    lateinit var btnPt3SetAd :Button

    lateinit var edtPt4Ref :EditText
    lateinit var edtPt4Ad :EditText
    lateinit var btnPt4SetAd :Button

    lateinit var edtPt5Ref :EditText
    lateinit var edtPt5Ad :EditText
    lateinit var btnPt5SetAd :Button

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
        return inflater.inflate(R.layout.fragment_remote_cal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtChannelAd = view.findViewById(R.id.et_cali_channel_sn_ad)
        btnAdcRead = view.findViewById(R.id.btn_cali_adc_read)
        btnAdcRead.setOnClickListener {
            //TODO(“修改到定时器1.5s”)
            var numStr = edtChannelAd.text.toString()
            if (numStr.isEmpty()) {
                numStr = "0"
            }
            var channelNum = numStr!!.toInt()
            if(channelNum <= 0)
                channelNum = 1
            else if(channelNum > 2)
                channelNum = 1

            EventBus.getDefault().post(
                McgfCtEvent(
                    McgfCtEvent.RwType.MsgSend,
                    MCFGCtHeaderCmdType.ReadAD
                ).put(channelNum));

        }
        edtAdcF = view.findViewById(R.id.et_cali_adc_value_f)
        edtAdcI = view.findViewById(R.id.et_cali_adc_value_i)

        edtChannelCal = view.findViewById(R.id.et_cali_channel_sn_cali)

        btnCaliRead = view.findViewById(R.id.btn_cali_read)
        btnCaliRead.setOnClickListener {

            var numStr = edtChannelCal.text.toString()
            if (numStr.isEmpty()) {
                numStr = "0"
            }
            var channelNum = numStr!!.toInt()
            if(channelNum <= 0)
                channelNum = 1
            else if(channelNum > 2)
                channelNum = 1


            EventBus.getDefault().post(
                McgfCtEvent(
                    McgfCtEvent.RwType.MsgSend,
                    MCFGCtHeaderCmdType.CaliParamRead
                ).put(channelNum));
        }
        btnCaliWrite = view.findViewById(R.id.btn_cali_write)
        btnCaliWrite.setOnClickListener {
            var mcfgCtPacket: McfgCtPacket = convertUiDataToPacket()
            EventBus.getDefault().post(
                McgfCtEvent(
                    McgfCtEvent.RwType.MsgSend,
                    MCFGCtHeaderCmdType.CaliParamWrite
                ).put(mcfgCtPacket));
        }

        edtSensorNum = view.findViewById(R.id.et_cali_sensor_no)
        edtCaliPts = view.findViewById(R.id.et_cali_points)

        edtPt1Ref = view.findViewById(R.id.et_cali_ref_1)
        edtPt1Ad = view.findViewById(R.id.et_cali_adc_1)
        btnPt1SetAd = view.findViewById(R.id.btn_cali_ok_1)
        btnPt1SetAd.setOnClickListener {
            edtPt1Ad.text = Editable.Factory.getInstance().newEditable(edtAdcI.text)
        }

        edtPt2Ref = view.findViewById(R.id.et_cali_ref_2)
        edtPt2Ad = view.findViewById(R.id.et_cali_adc_2)
        btnPt2SetAd = view.findViewById(R.id.btn_cali_ok_2)
        btnPt2SetAd.setOnClickListener {
            edtPt2Ad.text = Editable.Factory.getInstance().newEditable(edtAdcI.text)
        }

        edtPt3Ref = view.findViewById(R.id.et_cali_ref_3)
        edtPt3Ad = view.findViewById(R.id.et_cali_adc_3)
        btnPt3SetAd = view.findViewById(R.id.btn_cali_ok_3)
        btnPt3SetAd.setOnClickListener {
            edtPt3Ad.text = Editable.Factory.getInstance().newEditable(edtAdcI.text)
        }

        edtPt4Ref = view.findViewById(R.id.et_cali_ref_4)
        edtPt4Ad = view.findViewById(R.id.et_cali_adc_4)
        btnPt4SetAd = view.findViewById(R.id.btn_cali_ok_4)
        btnPt4SetAd.setOnClickListener {
            edtPt4Ad.text = Editable.Factory.getInstance().newEditable(edtAdcI.text)
        }

        edtPt5Ref = view.findViewById(R.id.et_cali_ref_5)
        edtPt5Ad = view.findViewById(R.id.et_cali_adc_5)
        btnPt5SetAd = view.findViewById(R.id.btn_cali_ok_5)
        btnPt5SetAd.setOnClickListener {
            edtPt5Ad.text = Editable.Factory.getInstance().newEditable(edtAdcI.text)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onMessageEvent(event: McgfCtEvent?) {
        if (event!!.rwType == McgfCtEvent.RwType.MsgRecv) {
            when(event.mcfgCtHeaderCmdType){
                //校准参数读取
                MCFGCtHeaderCmdType.CaliParamRead ->{
                    Log.i(tagApp, "McgfCtEvent.RwType.MsgRecv  CaliParamRead")
                    var mcfgCtPacket = __protocolHandler.mcfgCtPacketHandler.encoding(event.getMcfgCtPacket<McfgCtPacket>())
                    var calibrateRWPacket = __protocolHandler.mcfgCalibrateRWPacketHandler.decoding(mcfgCtPacket)
                    rwPacket__ = calibrateRWPacket
                    updateUi(calibrateRWPacket)
                    activity?.let { Snackbar.make(it.findViewById(R.id.fragment_remote_cal),"读通道${calibrateRWPacket.sensorNum}校准参数成功",Snackbar.LENGTH_SHORT).show() }
                }
                //读取AD
                MCFGCtHeaderCmdType.ReadAD ->{
                    Log.i(tagApp, "McgfCtEvent.RwType.MsgRecv  CaliParamRead")
                    var mcfgCtPacket = __protocolHandler.mcfgCtPacketHandler.encoding(event.getMcfgCtPacket<McfgCtPacket>())
                    var adValueRPacket = __protocolHandler.mcfgAdValueRPacketHandler.decoding(mcfgCtPacket)
                    updateUi(adValueRPacket)
                    activity?.let { Snackbar.make(it.findViewById(R.id.fragment_remote_cal),"读通道${adValueRPacket.sensorNum}AD值成功",Snackbar.LENGTH_SHORT).show() }
                }
                //校准参数设置返回
                MCFGCtHeaderCmdType.CaliParamWrite -> activity?.let {
                    Snackbar.make(it.findViewById(R.id.fragment_remote_cal)
                        ,"写校准参数成功"
                        , Snackbar.LENGTH_SHORT).show() }

                else -> {}
            }

        }

    }

    private fun updateUi(calibrateRWPacket: McfgCalibrateRWPacket) {
        //传感器序号
        edtSensorNum.text = Editable.Factory.getInstance().newEditable(calibrateRWPacket.sensorNum.toString())

        //标定点数
        edtCaliPts.text = Editable.Factory.getInstance().newEditable(
            calibrateRWPacket.calibratePointQuantity.toInt().and(0xFF).toString())

        //pt1
        var pt1 = parseAd8Bytes(calibrateRWPacket.caliPt1)
        edtPt1Ref.text = Editable.Factory.getInstance().newEditable(pt1!!.first.toString())
        edtPt1Ad.text = Editable.Factory.getInstance().newEditable(pt1!!.second.toString())

        //pt2
        var pt2 = parseAd8Bytes(calibrateRWPacket.caliPt2)
        edtPt2Ref.text = Editable.Factory.getInstance().newEditable(pt2!!.first.toString())
        edtPt2Ad.text = Editable.Factory.getInstance().newEditable(pt2!!.second.toString())

        //pt3
        var pt3 = parseAd8Bytes(calibrateRWPacket.caliPt3)
        edtPt3Ref.text = Editable.Factory.getInstance().newEditable(pt3!!.first.toString())
        edtPt3Ad.text = Editable.Factory.getInstance().newEditable(pt3!!.second.toString())


        //pt4
        var pt4 = parseAd8Bytes(calibrateRWPacket.caliPt4)
        edtPt4Ref.text = Editable.Factory.getInstance().newEditable(pt4!!.first.toString())
        edtPt4Ad.text = Editable.Factory.getInstance().newEditable(pt4!!.second.toString())

        //pt5
        var pt5 = parseAd8Bytes(calibrateRWPacket.caliPt5)
        edtPt5Ref.text = Editable.Factory.getInstance().newEditable(pt5!!.first.toString())
        edtPt5Ad.text = Editable.Factory.getInstance().newEditable(pt5!!.second.toString())

    }

    private fun updateUi(adValueRPacket: McfgAdValueRPacket) {
        //通道
        edtChannelAd.text = Editable.Factory.getInstance().newEditable(adValueRPacket.sensorNum.toString())

        //解析AD值 BE
        var adBE = parseAd8Bytes(adValueRPacket.adValue)

        edtAdcF.text =  Editable.Factory.getInstance().newEditable(adBE!!.first.toString())
        edtAdcI.text = Editable.Factory.getInstance().newEditable(adBE!!.second.toString())

    }

    private fun convertUiDataToPacket(): McfgCtPacket {

        var packet = rwPacket__

        //命令
        packet.cmdOpt = MCFGCtHeaderCmdType.CaliParamWrite.value.toByte()

        //通道、传感器序列号
        var numStr = edtSensorNum.text.toString()
        if (numStr.isEmpty()) {
            numStr = "0"
        }
        var channelNum = numStr!!.toInt()
        if(channelNum <= 0)
            channelNum = 1
        else if(channelNum > 2)
            channelNum = 1
        packet.sensorNum = channelNum

        //点数
        packet.calibratePointQuantity = try {
            edtSensorNum.text.toString()!!.toInt()!!.and(0xFF)!!.toByte()
        }catch (e :NumberFormatException){
            0x02
        }

        //pt1
        var pt1f = edtPt1Ref.text.toString()!!.toFloatOrNull()
        var pt1Ad = edtPt1Ad.text.toString()!!.toIntOrNull()
        if(pt1f != null && pt1Ad != null){
            var temp = getAd8Bytes(pt1f,pt1Ad)
            packet.caliPt1 = temp
        }

        //pt2
        var pt2f = edtPt2Ref.text.toString()!!.toFloatOrNull()
        var pt2Ad = edtPt2Ad.text.toString()!!.toIntOrNull()
        if(pt2f != null && pt2Ad != null){
            var temp = getAd8Bytes(pt2f,pt2Ad)
            packet.caliPt2 = temp
        }

        //pt3
        var pt3f = edtPt3Ref.text.toString()!!.toFloatOrNull()
        var pt3Ad = edtPt3Ad.text.toString()!!.toIntOrNull()
        if(pt3f != null && pt3Ad != null){
            packet.caliPt3 = getAd8Bytes(pt3f,pt3Ad)
        }

        //pt4
        var pt4f = edtPt4Ref.text.toString()!!.toFloatOrNull()
        var pt4Ad = edtPt4Ad.text.toString()!!.toIntOrNull()
        if(pt4f != null && pt4Ad != null){
            packet.caliPt4 = getAd8Bytes(pt4f,pt4Ad)
        }

        //pt5
        var pt5f = edtPt5Ref.text.toString()!!.toFloatOrNull()
        var pt5Ad = edtPt5Ad.text.toString()!!.toIntOrNull()
        if(pt5f != null && pt5Ad != null){
            packet.caliPt5 = getAd8Bytes(pt5f,pt5Ad)
        }

        return __protocolHandler.mcfgCtPacketHandler.decoding(__protocolHandler.mcfgCalibrateRWPacketHandler.encoding(packet))
    }

    private fun parseAd8Bytes(adValue :ByteArray, isBE: Boolean = true) : Pair<Float, Int> {
        if(adValue.size != 8)
            return Pair(0.0f,0)

        var adFArray = adValue.sliceArray(IntRange(0,3))
        var adIArray = adValue.sliceArray(IntRange(4,7))

        return if(isBE)
            Pair(adFArray.readFloatBE(),adIArray.readInt32BE(0))
        else
            Pair(adFArray.readFloatLE(),adIArray.readInt32LE(0))
    }

    private fun getAd8Bytes(fValue:Float,iValue:Int):ByteArray{
        var retArray = ByteArray(8)

        retArray.writeFloatBE(fValue)

        retArray.writeInt32BE(iValue.toLong().and(0xFFFFFFFF),4)

        return retArray
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}