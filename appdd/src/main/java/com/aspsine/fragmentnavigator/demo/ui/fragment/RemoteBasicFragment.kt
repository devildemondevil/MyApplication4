package com.aspsine.fragmentnavigator.demo.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.eventbus.McgfCtEvent
import com.aspsine.fragmentnavigator.demo.protocols.MCFGCtHeaderCmdType
import com.aspsine.fragmentnavigator.demo.protocols.ProtocolHandler
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgBasicParaRWPacket
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCtPacket
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.swallowsonny.convertextlibrary.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.experimental.or




/**
 * A simple [Fragment] subclass.
 * Use the [RemoteBasicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemoteBasicFragment : Fragment() {

    var tagApp = "FragmentNavigator"

    var __protocolHandler: ProtocolHandler = ProtocolHandler()

    var rawPacket__ = McfgBasicParaRWPacket()

    lateinit var fabRead: FloatingActionButton
    lateinit var fabWrite: FloatingActionButton

    lateinit var edtPanId : EditText
    lateinit var edtChannel : EditText
    lateinit var edtDeviceGroup :EditText
    lateinit var edtDeviceSN : EditText
    lateinit var edtSleepTime : EditText
    lateinit var tvFirmwareVer : TextView
    lateinit var edtPosition : EditText
    lateinit var edtBatteryData : EditText
    lateinit var edtUseDate : EditText
    lateinit var spAlarm : Spinner

    lateinit var edtAlarmDeadband : EditText

    lateinit var edtALarmHigh : EditText

    lateinit var edtAlarmLow : EditText

    lateinit var spStopCheck : Spinner

    lateinit var cbArmOrLen : CheckBox

    lateinit var edtArmLen : EditText

    lateinit var edtSoftState : EditText


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
        return inflater.inflate(R.layout.fragment_remote_basic, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtPanId = view.findViewById(R.id.et_basic_pan_id)

        edtChannel = view.findViewById(R.id.et_basic_channel)

        edtDeviceGroup = view.findViewById(R.id.et_basic_device_group)

        edtDeviceSN = view.findViewById(R.id.et_basic_device_sn)

        edtSleepTime = view.findViewById(R.id.et_basic_sleep_time)

        tvFirmwareVer = view.findViewById(R.id.tv_basic_firmware_version)

        edtPosition = view.findViewById(R.id.et_basic_position)
        edtBatteryData = view.findViewById(R.id.et_basic_batte_data)
        edtUseDate = view.findViewById(R.id.et_basic_use_date)

        //报警开关
        spAlarm = view.findViewById(R.id.sp_basic_alarm)
        ArrayAdapter.createFromResource(
            activity?.baseContext!!,
            R.array.remote_basic_stop_check_enable_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            spAlarm.adapter = adapter
        }


        edtAlarmDeadband = view.findViewById(R.id.et_basic_alarm_deadband)
        edtALarmHigh = view.findViewById(R.id.et_basic_alarm_high)
        edtAlarmLow = view.findViewById(R.id.et_basic_alarm_low)
        spStopCheck = view.findViewById(R.id.sp_basic_stop_check)
        ArrayAdapter.createFromResource(
            activity?.baseContext!!,
            R.array.remote_basic_alarm_enable_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            spStopCheck.adapter = adapter
        }

        cbArmOrLen = view.findViewById(R.id.cb_basic_arm_or_len)
        edtArmLen = view.findViewById(R.id.et_basic_arm_len)
        edtSoftState = view.findViewById(R.id.et_basic_soft_state)


        fabRead = view.findViewById(R.id.fab_remote_basic_read)
        fabRead.setOnClickListener(View.OnClickListener {
//                v -> Snackbar.make(v, "读 基本参数", Snackbar.LENGTH_SHORT).show()
            EventBus.getDefault().post(McgfCtEvent(
                McgfCtEvent.RwType.MsgSend,
                MCFGCtHeaderCmdType.BasicParamRead
            ).put(0));
        })
        fabWrite = view.findViewById(R.id.fab_remote_basic_write)
        fabWrite.setOnClickListener(View.OnClickListener {
//                v -> Snackbar.make(v, "写 基本参数", Snackbar.LENGTH_SHORT).show()
            var ret = convertUiDataToPacket();
            if(ret.first){
                EventBus.getDefault().post(McgfCtEvent(
                    McgfCtEvent.RwType.MsgSend,
                    MCFGCtHeaderCmdType.BasicParamWrite
                ).put(ret.second));
            }
        })
    }



    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onMessageEvent(event: McgfCtEvent?) {
        if (event!!.rwType == McgfCtEvent.RwType.MsgRecv) {
            when(event.mcfgCtHeaderCmdType){
                //参数读取
                MCFGCtHeaderCmdType.BasicParamRead ->{
                    Log.i(tagApp, "McgfCtEvent.RwType.MsgRecv  BasicParamRead")
                    var mcfgCtPacket = __protocolHandler.mcfgCtPacketHandler.encoding(event.getMcfgCtPacket<McfgCtPacket>())
                    var basicParaRWPacket = __protocolHandler.mcfgBasicParaRWPacketHandler.decoding(mcfgCtPacket)
                    rawPacket__ = basicParaRWPacket
                    updateUi(basicParaRWPacket)
                }
                //参数设置返回
                MCFGCtHeaderCmdType.BasicParamWrite -> activity?.let { Snackbar.make(it.findViewById(R.id.fragment_remote_basic),"写基本参数成功",Snackbar.LENGTH_SHORT).show() }
                else -> {}
            }

        }
    }

    private fun updateUi(mcfgBasicParaRWPacket: McfgBasicParaRWPacket){

        var positionRaw = mcfgBasicParaRWPacket.positionInfo.readStringBE(0,20,"ascii").trim().trimEnd { it <=' ' }

        Log.i(tagApp,"position raw:${positionRaw.toByteArray().toHexString()}")

        edtPosition.text = Editable.Factory.getInstance().newEditable(positionRaw)

        Log.i(tagApp,"position:${mcfgBasicParaRWPacket.positionInfo.toHexString()}")


        edtPanId.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.panId.and(0xFFFF).toString(16))

        edtChannel.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.channelNo.toInt().and(0xFF).toString())

        edtDeviceGroup.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.devGroupNo.toInt().and(0xFF).toString())

        edtDeviceSN.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.devNum.toInt().and(0xFF).toString())

        var alarmEnableIndex = mcfgBasicParaRWPacket.alarmEnable.toInt().and(0xFF)

        if(alarmEnableIndex < resources.getStringArray(R.array.remote_basic_alarm_enable_array).size){
            spAlarm.setSelection(alarmEnableIndex)
        }

        edtSleepTime.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.sleepTimes.and(0xFFFF).toUInt().toString())

        tvFirmwareVer.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.firmwareVer.and(0xFFFF).toUInt().toString())

        edtALarmHigh.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.alarmHigh.toString())

        edtAlarmLow.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.alarmLow.toString())

        edtAlarmDeadband.text = Editable.Factory.getInstance().newEditable(mcfgBasicParaRWPacket.alarmDeadband.and(0xFFFF).toString())

        edtBatteryData.text = Editable.Factory.getInstance().newEditable(bcdTime2String(mcfgBasicParaRWPacket.batteryData))

        Log.i(tag,"edtBatteryData : ${mcfgBasicParaRWPacket.batteryData.toHexString()}")

        edtUseDate.text = Editable.Factory.getInstance().newEditable(bcdTime2String(mcfgBasicParaRWPacket.useDate))
        Log.i(tag,"edtUseDate : ${mcfgBasicParaRWPacket.useDate.toHexString()}")

        var stopCheckIndex = mcfgBasicParaRWPacket.rsv2[0].toInt().and(0xFF)
        if(stopCheckIndex < resources.getStringArray(R.array.remote_basic_stop_check_enable_array).size){
            spStopCheck.setSelection(stopCheckIndex)
        }

        var alarmOrLen = mcfgBasicParaRWPacket.rsv2[1].toInt().and(0xFF)
        cbArmOrLen.isChecked = alarmOrLen > 0

        var alarmOrLenValue = mcfgBasicParaRWPacket.rsv2[2].toInt().and(0xFF) / 10.00
        edtArmLen.text = Editable.Factory.getInstance().newEditable(alarmOrLenValue.toString())

        var softState = mcfgBasicParaRWPacket.rsv2[3].toInt().and(0xFF)
        edtSoftState.text = Editable.Factory.getInstance().newEditable(softState.toString(10))

        activity?.let { Snackbar.make(it.findViewById(R.id.fragment_remote_basic),"读基本参数成功",Snackbar.LENGTH_SHORT).show() }
    }


    fun convertUiDataToPacket() : Pair<Boolean,McfgCtPacket>{


        var packet = rawPacket__

        packet.cmdOpt = MCFGCtHeaderCmdType.BasicParamWrite.value.toByte()

        //位置信息  Bytes:20      index:12~31
        var positionStr = edtPosition.text.toString().trim()
        //非空
        if(positionStr.isNotEmpty()){

            var le = positionStr.length

            //去除不可见字符
            positionStr = asciiDropControl(positionStr)

            var le1 = positionStr.length

            //长度大于 20  ，去掉20后
            if(positionStr.length > 20 * 2)
                positionStr = positionStr.dropLast(positionStr.length - 20*2)

            var position  =  ByteArray(20)

            position.writeStringBE(positionStr)

            packet.positionInfo = position
        }else{
            activity?.let { it -> var toast = Toast.makeText(
                it,"位置信息不能为空",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER,100,0)
            toast.show()}

            return Pair(false,McfgCtPacket())
        }



        //PAN_ID 号      Bytes:2     index:32~33
        packet.panId = getInt2BytesFromStr(edtPanId.text.toString(),16)

        //通道号       Bytes:1         index:34
        packet.channelNo =  getByteFromStr(edtChannel.text.toString())

        //仪表组号         Bytes:1     index:35
        packet.devGroupNo =  getByteFromStr(edtDeviceGroup.text.toString())

        //仪表编号     Bytes:1     index:36
        packet.devNum =  getByteFromStr(edtDeviceSN.text.toString())

        //报警使能     Bytes:1     index:37
        packet.alarmEnable = spAlarm.selectedItemPosition.toByte()

        //休眠时间     Bytes:2     index:38~39
        packet.sleepTimes = getInt2BytesFromStr(edtSleepTime.text.toString())

        //固件版本     Bytes:2     index:40~41
        packet.firmwareVer = getInt2BytesFromStr(tvFirmwareVer.text.toString())

        //报警上限    Bytes:4   index:42~45
        packet.alarmHigh = getLong4BytesFromStr(edtALarmHigh.text.toString())

        //报警下限     Bytes:4     index:46~49
        packet.alarmLow = getLong4BytesFromStr(edtAlarmLow.text.toString())

        //报警死区     Bytes:2     index:50~51
        packet.alarmDeadband = getInt2BytesFromStr(edtAlarmDeadband.text.toString())

        //电池更换日期   Bytes:6     index:52~57
        packet.batteryData = timeString2Bcd(edtBatteryData.text.toString())

        //投用日期     Bytes:6    index:58~63
        packet.useDate = timeString2Bcd(edtUseDate.text.toString())

        //停机检测
        packet.rsv2[0] = spStopCheck.selectedItemPosition.toByte()

        //臂长/冲程？
        var armOrLen : Byte = if (cbArmOrLen.isChecked){
            0x01
        }else {
            0x00
        }
        packet.rsv2[1] = armOrLen

        //值
        if(cbArmOrLen.isChecked && edtArmLen.text.isEmpty()){
            activity?.let { it -> var toast = Toast.makeText(
                it,"臂长不能为空",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,100,0)
                toast.show()}

            return Pair(false,McfgCtPacket())
        }

//        return try {
//            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
//            var date = simpleDateFormat.parse(string)
//            string == simpleDateFormat.format(date)
//        }catch (ex : ParseException ){
//            false
//        }
        var armOrLenValue  : Byte = try{
            (edtArmLen.text.toString().toFloat() * 10).toInt().toByte()
        }catch (ex :NumberFormatException ){
            0x00
        }

        packet.rsv2[2] = armOrLenValue

        return Pair(true,__protocolHandler.mcfgCtPacketHandler.decoding(__protocolHandler.mcfgBasicParaRWPacketHandler.encoding(packet)))
    }

    fun asciiDropControl(string: String) :String{

        var dropArray = asciiDropControl(string.toCharArray())

        return dropArray.map { it.toInt() }.joinToString("") { it.toString(16) }
    }

    fun asciiDropControl(charArray:CharArray) :CharArray{
        val byteList: MutableList<Char> = java.util.ArrayList()
        for ( ch in charArray){
            if(!ch.isISOControl()){
                byteList.add(ch)
            }
        }
        return byteList.toCharArray()
    }


    fun getInt2BytesFromStr(string: String,radixValue :Int = 10) : Int{
        if(string.isEmpty() || string.isBlank())
            return 0
        return Integer.parseInt(string,radixValue).and(0xFFFF)
    }

    fun getByteFromStr(string: String,radixValue : Int = 10):Byte{
        if(string.isEmpty() || string.isBlank())
            return 0
        return string.toByte(radixValue)
    }

    fun getLong4BytesFromStr(string: String,radixValue : Int = 10):Long{
        if(string.isEmpty() || string.isBlank())
            return 0
        return string.toLong(10)
    }


    fun bcdToDec(value: Byte): Byte {
        return (value / 16 * 10 + value % 16).toByte()
    }

    fun decToBcd(value: Byte): Byte {
        return (value / 10 * 16 + value % 10).toByte()
    }

    fun bcdTime2String(bcdArray :ByteArray) :String{
        var string = String()
        if(bcdArray.size != 6)
            return string
        for(byte in bcdArray.sliceArray(IntRange(0,3))){
            string += decToBcd(byte).toString()
        }
        string+="-"
        string += bcdArray[4].toInt().and(0xFF).shr(4).or(0x30).toChar()
        string += bcdArray[4].toInt().and(0xFF).and(0x0F).or(0x30).toChar()

        string+="-"
        string += bcdArray[5].toInt().and(0xFF).shr(4).or(0x30).toChar()
        string += bcdArray[5].toInt().and(0xFF).and(0x0F).or(0x30).toChar()

        return string
    }

    fun timeString2Bcd(string: String) :ByteArray{
        if(!stringIsDateFormat(string))
            return ByteArray(6)

//        McfgBasicParaRWPacket.batteryData : 02 00 02 00 06 16     2020634     20200616
//        McfgBasicParaRWPacket.useDate :     02 00 02 00 06 16

        val bcdArray: MutableList<Byte> = ArrayList()
        //yyyy - MM - dd
        //0  3   56   89
        //处理年
        for(chr in string.slice(IntRange(0,3))){
            var bte = (chr.toByte() - 0x30).toInt().and(0xFF).toByte()
            bcdArray.add(bte)
        }
        //处理月
//        x - y = x + ~y + 1
        var monthByte :Byte = (string[5] - 0x30).toInt().and(0xFF).shl(4).toByte()
        monthByte = monthByte.or((string[6] - 0x30).toByte())
        bcdArray.add(monthByte)
        //处理日
        var dayByte :Byte = (string[8] - 0x30).toInt().and(0xFF).shl(4).toByte()
        dayByte = dayByte.or((string[9] - 0x30).toByte())
        bcdArray.add(dayByte)

        return bcdArray.toByteArray()
    }

    /**
     * 判断是否符合时间格式
     */
    fun stringIsDateFormat(string: String):Boolean{
        return try {
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            var date = simpleDateFormat.parse(string)
            string == simpleDateFormat.format(date)
        }catch (ex : ParseException ){
            false
        }
    }
    

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
