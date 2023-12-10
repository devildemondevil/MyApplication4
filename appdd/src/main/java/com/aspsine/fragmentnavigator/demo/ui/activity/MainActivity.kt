package com.aspsine.fragmentnavigator.demo.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.fragment.FragmentNavigator
import androidx.fragment.app.FragmentManager;
//import androidx.navigation.fragment.FragmentNavigator;
//import androidx.navigation.fragment.NavHostFragment;
import com.aspsine.fragmentnavigator.FragmentNavigator
import com.aspsine.fragmentnavigator.demo.*
import com.aspsine.fragmentnavigator.demo.broadcast.BroadcastManager
import com.aspsine.fragmentnavigator.demo.eventbus.McgfCtEvent
import com.aspsine.fragmentnavigator.demo.eventbus.MessageZigBeeFrameEvent
import com.aspsine.fragmentnavigator.demo.protocols.*
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.MCFGCmdOneRsv1Packet
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCmdOneRsv21Packet
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCtPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdAndQpPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdRespPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeCFPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeERIPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.ZigBeePacket
import com.aspsine.fragmentnavigator.demo.ui.adapter.FragmentAdapter
import com.aspsine.fragmentnavigator.demo.ui.widget.BottomNavigatorView
import com.aspsine.fragmentnavigator.demo.ui.widget.BottomNavigatorView.OnBottomNavigatorViewItemClickListener
import com.aspsine.fragmentnavigator.demo.utils.SharedPrefUtils
import com.sjx.serialhelperlibrary.OnUsbDataListener
import com.sjx.serialhelperlibrary.OnUsbStatusChangeListener
import com.sjx.serialhelperlibrary.SerialConfig
import com.sjx.serialhelperlibrary.SerialHelper
//import com.swallowsonny.convertextlibrary.toHexString
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.concurrent.fixedRateTimer


class MainActivity : AppCompatActivity(), OnBottomNavigatorViewItemClickListener {

    var tagApp = "FragmentNavigator"

    //协议工作模式
    var __workMode  = WorkModeType.WorkMode_A11

    //源网络地址16位
    var __srcNetAddr16 : Int = 0
    //源地址64位
    var __srcAddr64 : ByteArray = ByteArray(8)

    //从未收到  zigbee 传输完成帧
    var __isFistRevAPI_ZigbeeTS  : Boolean = true

    //是否连接成功， 收到请求，发送请求，回复确认， 第一次发送心跳，回复确认
    //                1 ,    2 ,    3 ,        4   ,       5  = 成功
    var __connPairStep :Int = 0

    //是否是退出配置
    var __isDisCfg :Boolean = false

    private var __tmrHeartBeat: Timer = Timer()

    //编码/解码各种协议
    var __protocolHandler: ProtocolHandler = ProtocolHandler()

    //界面更新 handler
    private val handler = Handler(){
        when(it.what){
            0x01 ->{//USB
                var objPair : Pair<Int,String> = it.obj as Pair<Int, String>
                if(objPair.first == 5){
                    Toast.makeText(baseContext,objPair.second,Toast.LENGTH_SHORT).show()
                }else if(objPair.first in 6..7){
                    __disCfgMenu?.isVisible = false
                    __connPairStep = 0
                    Toast.makeText(baseContext,objPair.second,Toast.LENGTH_SHORT).show()
                }
            }
            0x02 ->{//zigbee设备是否连接
                var objInt :Int = it.obj as Int
                if(objInt == 1){//有设备接入
                    Toast.makeText(baseContext,"有设备请求接入",Toast.LENGTH_SHORT).show()
                } else if(objInt == 5){//连接成功
                    __disCfgMenu?.isVisible = true
                    Toast.makeText(baseContext,"设备接入成功",Toast.LENGTH_LONG).show()
                }
            }
        }

        true
    }

    private var mNavigator: com.aspsine.fragmentnavigator.FragmentNavigator? = null// 底部导航
    private var bottomNavigatorView: BottomNavigatorView? = null

    private var mLoginMenu: MenuItem? = null
    private var mLogoutMenu: MenuItem? = null

    private var __disCfgMenu : MenuItem? = null

    private  lateinit var __serialHelper: SerialHelper// 串口

    private  var __recvDataCache: ByteArray = ByteArray(0)// 接受数据，负责从串口读

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //EventBus 注册
        EventBus.getDefault().register(this)

        mNavigator = FragmentNavigator(supportFragmentManager, FragmentAdapter(), R.id.container)
        mNavigator!!.setDefaultPosition(DEFAULT_POSITION)
        mNavigator!!.onCreate(savedInstanceState)
        bottomNavigatorView = findViewById<View>(R.id.bottomNavigatorView) as BottomNavigatorView
        if (bottomNavigatorView != null) {
            bottomNavigatorView!!.setOnBottomNavigatorViewItemClickListener(this)
        }

        //串口配置
        var serialConfig = SerialConfig()
        serialConfig.isAutoConnect = true //默认连接第一个
        serialConfig.baudRate = 9600
        serialConfig.readInterval = 2 // ms，子线程读取，休眠间隔，双缓冲读取与写速率调整，默认10ms
        serialConfig.doubleBufferSize = 20 // 双缓冲容量大小，循环覆盖缓存
        serialConfig.dataMaxSize = 30000  // 当拼接数据未找到完整帧，长度大于30000清空，可根据实际情况适当调整


        __serialHelper = object :SerialHelper(serialConfig){
            override fun isFullFrame(data: ByteArray): IntArray {
                return intArrayOf(0,data.size)
            }
        }
        __serialHelper.addOnUsbDataListener(object : OnUsbDataListener {
            override fun onDataReceived(bytes: ByteArray) {
//                Log.i(tagApp,"onDataReceived  :${bytes.toHexString()}")
                __recvDataCache += bytes
                //处理接受数据 ？ 每一次都进入新线程？
                Thread{
                    parseRevData()
                }.start()
            }

            override fun onDataError(e: Exception?) {
                runOnUiThread {
//                    addText("onDataError: ${e?.message}")
                    Log.i(tagApp,"DataError")
                }
            }
        })

        __serialHelper.addOnUsbStatusChangeListener(object : OnUsbStatusChangeListener {
            override fun onUsbDeviceAttached() {
                //0
                Log.i(tagApp,"onUsbDeviceAttached")
            }

            override fun onUsbDeviceDetached() {
                //1
                resetInitPara()
                Log.i(tagApp,"onUsbDeviceDetached")
            }

            override fun onPermissionGranted() {
                //2
                Log.i(tagApp,"onPermissionGranted")
            }

            override fun onPermissionDenied() {
                //3
                Log.i(tagApp,"onPermissionDenied")
            }

            override fun onDeviceNotSupport() {
                //4
                Log.i(tagApp,"onDeviceNotSupport")
            }

            override fun onUsbConnect(usbDevice: UsbDevice) {
                Log.i(tagApp,"onUsbConnect ${usbDevice.deviceName}")
                var obj = Pair<Int,String>(5,"onUsbConnect ${usbDevice.deviceName}")
                var msg = Message.obtain(handler,0x01,obj)
                handler.sendMessage(msg)
            }

            override fun onUsbConnectError(e: Exception) {
                //6
                resetInitPara()
                Log.i(tagApp,"onUsbConnectError")
                var obj = Pair<Int,String>(6,"onUsbConnectError")
                var msg = Message.obtain(handler,0x01,obj)
                handler.sendMessage(msg)
                //类似login的通知到各个fragment，控制按键
            }

            override fun onUsbDisconnect() {
                //7
                resetInitPara()
                Log.i(tagApp,"onUsbDisconnect")
                var obj = Pair<Int,String>(7,"onUsbDisconnect")
                var msg = Message.obtain(handler,0x01,obj)
                handler.sendMessage(msg)
                //类似login的通知到各个fragment，控制按键
            }
        })
        __serialHelper.onCreate(this)

        setCurrentTab(mNavigator!!.currentPosition)

        BroadcastManager.register(this, mLoginStatusChangeReceiver, Action.LOGIN, Action.LOGOUT)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        __disCfgMenu = menu.findItem(R.id.action_dis_cfg)
        mLoginMenu = menu.findItem(R.id.action_login)
        mLogoutMenu = menu.findItem(R.id.action_logout)

        __disCfgMenu!!.isVisible = false
//        __disCfgMenu!!.title = "未连接"
//        __disCfgMenu!!.isEnabled = false

        mLoginMenu!!.isVisible = false
        mLogoutMenu!!.isVisible = false

//        toggleMenu(SharedPrefUtils.isLogin(this))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mNavigator!!.onSaveInstanceState(outState)
    }

    /**
     * 处理MCFG Event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = false)
    fun onMessageEvent(event: McgfCtEvent?) {
        Log.i(tagApp, "recv McgfCtEvent ${event?.rwType}")
        if (event != null) {
            //MCFG 发送信息
            if(event.rwType == McgfCtEvent.RwType.MsgSend){
                when(event.mcfgCtHeaderCmdType){
                    //读取基本信息 参数帧
                    MCFGCtHeaderCmdType.BasicParamRead,
                    MCFGCtHeaderCmdType.ExtendParamRead-> {
                        mcfgReadCmdMsgWithPara(event.mcfgCtHeaderCmdType)
                    }

                    //读取参数  校准帧
                    MCFGCtHeaderCmdType.ReadAD ->{
                        var channelNum = event.getInt().and(0xFF).toByte()
                        mcfgReadCmdMsgWithCal(event.mcfgCtHeaderCmdType,channelNum)
                    }
                    MCFGCtHeaderCmdType.CaliParamRead ->{
                        var channelNum = event.getInt().and(0xFF).toByte()
                        mcfgReadCmdMsgWithCal(event.mcfgCtHeaderCmdType,channelNum)
                    }
                    
                    //写入基本信息 参数帧
                    MCFGCtHeaderCmdType.BasicParamWrite,
                    MCFGCtHeaderCmdType.ExtendParamWrite->{
                        event.getMcfgCtPacket<McfgCtPacket>()?.let { mcfgWriteCmdMsg(it,A11HeaderDataType.ParameterMcfgCt) }
                    }

                    //写入信息  校准帧
                    MCFGCtHeaderCmdType.CaliParamWrite ->{
                        event.getMcfgCtPacket<McfgCtPacket>()?.let { mcfgWriteCmdMsg(it,A11HeaderDataType.CalibrationMcfgCt) }
                    }

                    else -> {}
                }
            }
        }
    }
    /**
     * 处理MessageZigBeeFrameEvent
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = false)
    fun onMessageEvent(event: MessageZigBeeFrameEvent?) {
        Log.i(tagApp, "recv MessageZigBeeFrameEvent ${event?.apiFrameType}")
        when(event!!.apiFrameType){
            ApiFrameType.API_ATCommand, ApiFrameType.API_ATCommandQP->{
                event.getApiAtCmdAndQpPacket<ApiZigbeeATCmdAndQpPacket>()?.let {
                    var array = packageTotalZigbee(__protocolHandler.apiZigbeeATCmdPacket.encoding(it))
//                    Log.i(tagApp,"API_ATCommand  :${array.toHexString()}")
                    serialWriteProtocolData(array)
                }
            }

            else -> {}
        }
    }



    fun  byteArrayOfInts(vararg ints :Int)=ByteArray(ints.size){
        pos->ints[pos].toByte()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_dis_cfg ->{
                __isDisCfg = true
                packageDisCfg()
                resetInitPara()
                __connPairStep = 0
                __disCfgMenu?.isVisible = false
                true
            }
//            R.id.action_login -> {
////                startActivity(Intent(this, LoginActivity::class.java))
////                var tet = "7E00100304"//
////                __serialHelper.write(tet.hex2ByteArray())
////                var testData = byteArrayOfInts(0x5F,0xFF,0x7E,0x00,0x05,0x08,0x01,0x4E,0x4A)
//                var testData = byteArrayOfInts(0x7e,0x00,0x1e,0x91,0xd0
//                    ,0xcf,0x5e,0xff,0xfe,0x7c,
//                    0x71,0xb4,0x58,0xd1,0x00,
//                    0x00,0x00,0x13,0x00,0x00,
//                    0x02,0x81,0xd1,0x58,0xb4,
//                    0x71,0x7c,0xfe,0xff,0x5e,
//                    0xcf,0xd0,0x80,0xd0)
//                var testData2 = byteArrayOfInts(0x7E,0x00,0x1E,0x91,0xD0
//                    ,0xCF,0x5E,0xFF,0xFE,0x7C
//                    ,0x71,0xB4,0x25,0xB1,0xE8
//                    ,0xE8,0x00,0x11,0x18,0x57
//                    ,0x02,0x00,0x01,0x00,0x02
//                    ,0x00,0x01,0x03,0x01,0x00
//                    ,0x90,0x00,0x00,0x13)
//                __recvDataCache += testData2
//                parseRevData()
//                return true
//            }
//            R.id.action_logout -> {
//                logout()
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        BroadcastManager.unregister(this, mLoginStatusChangeReceiver)
        super.onDestroy()
        //EventBus 注销
        EventBus.getDefault().unregister(this)
        __serialHelper.onDestory()
    }

    override fun onBottomNavigatorViewItemClick(position: Int, view: View?) {
        setCurrentTab(position)
    }

    private fun logout() {
        SharedPrefUtils.logout(this)
        BroadcastManager.sendLogoutBroadcast(this, 1)
    }

    private fun onUserLogin(position: Int) {
        if (position == -1) {
            resetAllTabsAndShow(mNavigator!!.currentPosition)
        } else {
            resetAllTabsAndShow(position)
        }
        toggleMenu(true)
    }

    private fun onUserLogout(position: Int) {
        if (position == -1) {
            resetAllTabsAndShow(mNavigator!!.currentPosition)
        } else {
            resetAllTabsAndShow(position)
        }
        toggleMenu(false)
    }

    private fun setCurrentTab(position: Int) {
        mNavigator!!.showFragment(position)
        bottomNavigatorView!!.select(position)
    }

    private fun resetAllTabsAndShow(position: Int) {
        mNavigator!!.resetFragments(position, true)
        bottomNavigatorView!!.select(position)
    }

    private fun toggleMenu(login: Boolean) {
        if (login) {
            mLoginMenu!!.isVisible = true
            mLogoutMenu!!.isVisible = true
        } else {
            mLoginMenu!!.isVisible = true
            mLogoutMenu!!.isVisible = true
        }
    }

    private val mLoginStatusChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (!TextUtils.isEmpty(action)) {
                val position = intent.getIntExtra("EXTRA_POSITION", -1)
                if (action == Action.LOGIN) {
                    onUserLogin(position)
                } else if (action == Action.LOGOUT) {
                    onUserLogout(position)
                }
            }
        }
    }

    private fun parseRevData() {
//        Log.i(tagApp,"parseRevData  :${recvDataCache.toHexString()}")


        //Zigbee API 结构
//        开始帧分隔符       长度                   帧数据           校验
//        Byte1            Byte 2~3[MSB,LSB]     Byte 4-n         Byte n+1
//        0x7E                                 cmdId  cmdData

        val startDelimiter : Byte = 0x7E

        //查找帧起始分隔符index
        var startIndex =  __recvDataCache.indexOfFirst { it == startDelimiter }

        //未找到起始分隔符，清空已接受数据
        if(__recvDataCache.isNotEmpty() && startIndex == -1){
            __recvDataCache = ByteArray(0)
//            Log.i(tagApp,"Drop  :${__recvDataCache.toHexString()}")
            return
        }

        //删除分隔符前面数据
        if(startIndex > 0 ){
            var dataLength = __recvDataCache.size;
            Log.i(tagApp,"删除分隔符前面数据 startIndex :${startIndex} recvDataCache.size:${__recvDataCache.size} ")
            __recvDataCache = __recvDataCache.sliceArray(IntRange(startIndex,dataLength - 1))
        }

        //获取长度
        var frameLength : Int = 0
        if(__recvDataCache.size >= 3){
            var high8 : Int = __recvDataCache[1].toInt().and(0xFF).shl(8)
            var low8: Int = __recvDataCache[2].toInt().and(0xFF)
            frameLength = high8.or(low8)
        }

        //获取 头部 + 长度+ 数据+ 校验 总长度
        var totalFrameLength : Int = 1 + 2 + frameLength + 1
        if(__recvDataCache.size < totalFrameLength ){
            Log.i(tagApp,"totalFrameLength is less :${totalFrameLength} recvDataSize :${__recvDataCache.size}")
            return
        }

        //完整一帧数据
        var oneFrame = __recvDataCache.sliceArray(IntRange(0,totalFrameLength - 1))
//        Log.i(tagApp,"Full frame      :${oneFrame.toHexString()}")


        //剩余数据删除前部
        __recvDataCache = __recvDataCache.sliceArray(IntRange(totalFrameLength,__recvDataCache.size-1))

//        Log.i(tagApp,"after slice   :${recvDataCache.toHexString()}")

        //获取帧的数据部分
        var frameData = oneFrame.sliceArray(IntRange(3,totalFrameLength -1 -1))
//        Log.i(tagApp,"oneFrame.frameData      :${frameData.toHexString()}")

        //获取帧校验字节
        var frameCheckByte = oneFrame.last()
        var frameCheckByteArray = ByteArray(1)
        frameCheckByteArray.set(0,frameCheckByte)
//        Log.i(tagApp,"frameCheckByte      :${frameCheckByteArray.toHexString()}")

        //校验错误
        if(!crcCheck(frameData,frameCheckByte)) {
            Log.i(tagApp,"crcCheck error")
            return
        }

        //正常
//        Log.i(tagApp,"crcCheck ok")

        //处理API Data 部分
//        Log.i(tagApp," --> ProtocolData :${oneFrame.toHexString()}")
        when(__workMode){
            //A11部分
            WorkModeType.WorkMode_A11 ->        dealWithWorkModeA11(oneFrame)
            else -> {}
        }

//        EventBus.getDefault().postSticky("${byteArray.toHexString()}");
//        Thread.sleep(10000)
//        runOnUiThread {
//            Log.i(tagApp,"parseRevData 2 :${byteArray.toHexString()}")
//        }
    }

    private fun crcCheck(frameDataPart: ByteArray,check : Byte) : Boolean{
        var checkSum :Byte =  frameDataPart.sum().and(0xFF).toByte()
        checkSum = (0xFF - checkSum).toByte()
        return checkSum == check
//        var temp :Byte = checkSum.and(0xFF.toByte()).toByte()
//        x - y = x + ~y + 1
//        temp  = (0xFF + temp.inv() + 0x01).toByte()
    }

    private fun dealWithWorkModeA11(frameData :ByteArray){

        var zigBeePacket =  __protocolHandler.zigBeePacketHandler.decoding(frameData)

        Log.i(tagApp,"+++++++++ zigBeePacket +++++++++")
        Log.i(tagApp,"  zigBeePacket.startDelimiter    :${zigBeePacket.startDelimiter.toInt().and(0xFF).toInt().toString(16)}")
        Log.i(tagApp,"  zigBeePacket.length    :${zigBeePacket.length.toInt().and(0xFFFF).toInt().toString(16)}")
//        Log.i(tagApp,"  zigBeePacket.frameData    :${zigBeePacket.frameData.toHexString()}")
        Log.i(tagApp,"      zigBeePacket.ApiData.apiId    :${zigBeePacket.apiId.toInt().and(0xFF).toInt().toString(16)}")
//        Log.i(tagApp,"      zigBeePacket.ApiData.apiData    :${zigBeePacket.apiData.toHexString()}")
        Log.i(tagApp," zigBeePacket.checkSum    :${zigBeePacket.checkSum.toInt().and(0xFF).toInt().toString(16)}")
        Log.i(tagApp,"--------- zigBeePacket ---------")


        var id = zigBeePacket.apiId.toInt().and(0xFF).toUInt()
        var strid = id.toString()
        var idu = strid.toInt()
        var apiId = ApiFrameType.fromValue(idu)

        when(apiId){
            ApiFrameType.API_ATResponse ->{
//                Log.i(tagApp,"API_ATResponse recvived : ${zigBeePacket.frameData.toHexString()}")
                var atRespPacket = __protocolHandler.apiZigbeeATCmdRespPacketHandler.decoding(zigBeePacket.frameData)
                dealWithApiAtResp(atRespPacket)
            }
            ApiFrameType.API_ModemStatus -> {
//                Log.i(tagApp,"API_ModemStatus recvived : ${zigBeePacket.apiData.toHexString()}")
            }
            ApiFrameType.API_ZigbeeTS ->{
                //第一次接受开启 心跳定时任务  而且非退出配置情况
                if(__isFistRevAPI_ZigbeeTS && !__isDisCfg){
                    __isFistRevAPI_ZigbeeTS = false;
                    if(!__isFistRevAPI_ZigbeeTS){
//                        __tmrHeartBeat.schedule(__tmrHeartBeatTask, 0, 5000)
                        tmrHeartBeatStart()
                        Log.i(tagApp,"__tmrHeartBeat start")
                    }
                }

                if(__connPairStep == 2)
                    __connPairStep = 3
                else if (__connPairStep == 4){
                    __connPairStep = 5
                    var msg = Message.obtain(handler,0x02,5)
                    handler.sendMessage(msg)
                }

//                __disCfgMenu!!.title = "退出配置"
//                __disCfgMenu!!.isEnabled = true
//                Log.i(tagApp,"API_ZigbeeTS recvived : ${zigBeePacket.apiData.toHexString()}")
            }
            ApiFrameType.API_ZigbeeRP ->{
//                Log.i(tagApp,"API_ZigbeeRP recvived : ${zigBeePacket.apiData.toHexString()}")
            }
            ApiFrameType.API_ZigbeeERI ->{
                var eriPacket = __protocolHandler.apiZigbeeERIPacketHandler.decoding(zigBeePacket.frameData)
                dealA11WithEri(eriPacket)
            }
            ApiFrameType.API_ZigbeeCF ->{
                dealA11WithCf(frameData)
            }

            else -> {}
        }
    }

    private fun dealWithApiAtResp(atRespPacket: ApiZigbeeATCmdRespPacket) {
        EventBus.getDefault().post(
            MessageZigBeeFrameEvent(
                ApiFrameType.API_ATResponse
            ).put(atRespPacket));
    }

    private fun dealA11WithEri(eriPacket : ApiZigbeeERIPacket){

        __srcNetAddr16 = eriPacket.srcNetAddr
        __srcAddr64 = eriPacket.srcAddr

        Log.i(tagApp,"+++++++++ API_ZigbeeERI +++++++++")
        Log.i(tagApp,"  API_ZigbeeERI.frameType   :${eriPacket.frameType.toInt().and(0xFF).toInt().toString(16)}")
//        Log.i(tagApp,"  API_ZigbeeERI.srcAddr   :${eriPacket.srcAddr.toHexString()}")
        Log.i(tagApp,"  API_ZigbeeERI.srcNetAddr   :${eriPacket.srcNetAddr.toInt().and(0xFFFF).toInt().toString(16)}")
        Log.i(tagApp,"  API_ZigbeeERI.srcEndpoint   :${eriPacket.srcEndpoint.toInt().and(0xFF).toInt().toString(16)}")
        Log.i(tagApp,"  API_ZigbeeERI.destEndpoint   :${eriPacket.destEndpoint.toInt().and(0xFF).toInt().toString(16)}")
        Log.i(tagApp,"  API_ZigbeeERI.clusterId   :${eriPacket.clusterId.toInt().and(0xFFFF).toInt().toString(16)}")
        Log.i(tagApp,"  API_ZigbeeERI.profileId   :${eriPacket.profileId.toInt().and(0xFFFF).toInt().toString(16)}")
        Log.i(tagApp,"  API_ZigbeeERI.recvOpt   :${eriPacket.recvOpt.toInt().and(0xFF).toInt().toString(16)}")
//        Log.i(tagApp,"  API_ZigbeeERI.recvData   :${eriPacket.recvData.toHexString()}")
        Log.i(tagApp,"--------- API_ZigbeeERI ---------")


        var a11TotalPacket = __protocolHandler.a11TotalPacketHandler.decoding(eriPacket.recvData)

        Log.i(tagApp,"+++++++++ a11 Protocol +++++++++")
        Log.i(tagApp,"  a11Header.ruleType   :${a11TotalPacket.packetHeader.ruleType.toInt().and(0xFFFF).toInt().toString(16)}")
        Log.i(tagApp,"  a11Header.manuId     :${a11TotalPacket.packetHeader.manuId.toInt().and(0xFFFF).toInt().toString(16)}")
        Log.i(tagApp,"  a11Header.instrument :${a11TotalPacket.packetHeader.instrument.toInt().and(0xFFFF).toInt().toString(16)}")
        Log.i(tagApp,"  a11Header.groupId    :${a11TotalPacket.packetHeader.groupId.toInt().and(0xFF).toInt().toString(16)}")
        Log.i(tagApp,"  a11Header.sn         :${a11TotalPacket.packetHeader.sn.toInt().and(0xFF).toInt().toString(16)}")
        Log.i(tagApp,"  a11Header.dataType   :${a11TotalPacket.packetHeader.dataType.toInt().and(0xFFFF).toInt().toString(16)}")
//        Log.i(tagApp,"      a11 payload Data  :${a11TotalPacket.dataPart.toHexString()}")
        Log.i(tagApp,"--------- a11 Protocol ---------")

        var dataTypeInt = a11TotalPacket.packetHeader.dataType.toInt().and(0xFFFF).toUInt().toString().toInt()
        var dataType = A11HeaderDataType.fromValue(dataTypeInt)

        when(dataType){
            A11HeaderDataType.ParameterMcfgCt, A11HeaderDataType.CalibrationMcfgCt    ->{
                //MCFG/CT
                var mcfgCtPacket = __protocolHandler.mcfgCtPacketHandler.decoding(eriPacket.recvData)
                Log.i(tagApp,"+++++++++ a11 Protocol +++++++++")
                Log.i(tagApp,"mcfgCtPacketHeader.cmdType   :${mcfgCtPacket.cmdType.toInt().and(0xFF).toInt().toString(16)}")
//                Log.i(tagApp,"mcfgCtPacketHeader.FollowUpData   :${mcfgCtPacket.FollowUpData.toHexString()}")
                Log.i(tagApp,"+++++++++ a11 Protocol +++++++++")
                dealMcfgPacket(mcfgCtPacket)
            }
            else ->{
                Log.i(tagApp,"A11HeaderDataType  Not contains")

            }
        }
    }

    private fun dealMcfgPacket(mcfgCtPacket : McfgCtPacket){

        var cmdTypeId = mcfgCtPacket.cmdType.toInt().and(0xFF).toUInt().toString().toInt()
        var cmdType = MCFGCtHeaderCmdType.fromValue(cmdTypeId)
        when(cmdType){

            MCFGCtHeaderCmdType.ConnetReq -> dealWithMCFGCtHeaderCmdTypeConnetReq(mcfgCtPacket)

            //写入状态返回
            MCFGCtHeaderCmdType.BasicParamWrite,
            MCFGCtHeaderCmdType.ExtendParamWrite,
            MCFGCtHeaderCmdType.CaliParamWrite->dealWithMCFGCtWriteRet(cmdType)

            //读取信息返回
            MCFGCtHeaderCmdType.BasicParamRead,
            MCFGCtHeaderCmdType.ExtendParamRead,
            MCFGCtHeaderCmdType.ReadAD,
            MCFGCtHeaderCmdType.CaliParamRead -> dealWithMCFGCtReadRet(cmdType,mcfgCtPacket)
            
            else ->{
                if (cmdType != null) {
                    Log.i(tagApp,"MCFGCtHeaderCmdType  Not contains , ${cmdType.value.toString(16)}")
                }
            }
        }
    }

    private fun dealWithMCFGCtReadRet(cmdType: MCFGCtHeaderCmdType,mcfgCtPacket: McfgCtPacket){
        EventBus.getDefault().postSticky(McgfCtEvent(
            McgfCtEvent.RwType.MsgRecv,cmdType
        ).put(mcfgCtPacket));
    }

    private fun dealWithMCFGCtHeaderCmdTypeCaliParamRead(mcfgCtPacket: McfgCtPacket) {
        EventBus.getDefault().postSticky(McgfCtEvent(
            McgfCtEvent.RwType.MsgRecv,
            MCFGCtHeaderCmdType.CaliParamRead
        ).put(mcfgCtPacket));
    }

    private fun dealWithMCFGCtHeaderCmdTypeAdRead(mcfgCtPacket: McfgCtPacket) {
        EventBus.getDefault().postSticky(McgfCtEvent(
            McgfCtEvent.RwType.MsgRecv,
            MCFGCtHeaderCmdType.ReadAD
        ).put(mcfgCtPacket));
    }

    private fun dealWithMCFGCtHeaderCmdTypeBasicParamRead(mcfgCtPacket : McfgCtPacket){
//        var mcfgCtPacketBinary = __protocolHandler.mcfgCtPacketHandler.encoding(mcfgCtPacket)
//        var basicPara = __protocolHandler.mcfgBasicParaRWPacketHandler.decoding(mcfgCtPacketBinary)
//
//        Log.i(tagApp,"McfgBasicParaRWPacket.positionInfo : ${basicPara.positionInfo.toHexString()}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.panId : ${basicPara.panId.and(0xFFFF).toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.channelNo : ${basicPara.channelNo.toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.devGroupNo : ${basicPara.devGroupNo.toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.devNum : ${basicPara.devNum.toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.alarmEnable : ${basicPara.alarmEnable.toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.sleepTimes : ${basicPara.sleepTimes.and(0xFFFF).toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.firmwareVer : ${basicPara.firmwareVer.and(0xFFFF).toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.alarmHigh : ${basicPara.alarmHigh.and(0xFFFFFFFF).toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.alarmLow : ${basicPara.alarmLow.and(0xFFFFFFFF).toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.alarmDeadband : ${basicPara.alarmDeadband.and(0xFFFF).toString(16)}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.batteryData : ${basicPara.batteryData.toHexString()}")
//        Log.i(tagApp,"McfgBasicParaRWPacket.useDate : ${basicPara.useDate.toHexString()}")

        EventBus.getDefault().postSticky(McgfCtEvent(
            McgfCtEvent.RwType.MsgRecv,
            MCFGCtHeaderCmdType.BasicParamRead
        ).put(mcfgCtPacket));
    }

    private fun dealWithMCFGCtHeaderCmdTypeExtendParamRead(mcfgCtPacket : McfgCtPacket){
        EventBus.getDefault().postSticky(McgfCtEvent(
            McgfCtEvent.RwType.MsgRecv,
            MCFGCtHeaderCmdType.ExtendParamRead
        ).put(mcfgCtPacket));
    }

    private fun dealWithMCFGCtWriteRet(cmdType: MCFGCtHeaderCmdType) {
        EventBus.getDefault().postSticky(McgfCtEvent(
            McgfCtEvent.RwType.MsgRecv,cmdType
        ).put(0));
    }

    private fun dealWithMCFGCtHeaderCmdTypeConnetReq(mcfgCtPacket : McfgCtPacket){
        Log.i(tagApp,"dealWithMCFGCtHeaderCmdTypeConnetReq")


        tmrHeartBeatStop()
        __isFistRevAPI_ZigbeeTS = true
        __isDisCfg = false

        if(__connPairStep == 0){
            __connPairStep = 1
            var msg = Message.obtain(handler,0x02,1)
            handler.sendMessage(msg)
        }

        //1、回复管理/命令模式

        //转换为二进制
        var mcfgTotalBinary = __protocolHandler.mcfgCtPacketHandler.encoding(mcfgCtPacket)

//        //解析接受信息
//        var mcfgConnectPacket = __protocolHandler.mcfgCmdOneRsvOnePacketHandler.decoding(mcfgTotalBinary)
//
//        //mcfgConn 转 二进制
//        var binaryData =  __protocolHandler.mcfgCmdOneRsvOnePacketHandler.coding(mcfgConnectPacket)

        // 使用 mcfgConn 初始化 mcfgConnACk
//        var ackPacket1 = __protocolHandler.mcfgCmdOneRsv21Handler.decoding(mcfgTotalBinary)
        var ackPacket = McfgCmdOneRsv21Packet()

        //设置 配置 为 管理模式
        ackPacket.cmdOpt = MCFGCtHeaderCmdType.AdminMode.value.toByte()

        var A11binaryData =  packageA11Part(__protocolHandler.mcfgCmdOneRsv21Handler.encoding(ackPacket),A11HeaderDataType.ParameterMcfgCt)
        var cfBinaryData = packageZigbeeCFPacket(A11binaryData,0x01)
        var zigbeeBinaryData = packageTotalZigbee(cfBinaryData)

//        Log.i(tagApp,"Generate zigbeeBinaryData :${zigbeeBinaryData.toHexString()}")

        serialWriteProtocolData(zigbeeBinaryData)
        if(__connPairStep == 1){
            __connPairStep = 2
        }
    }


    private fun packageA11Part(a11DataBinary :ByteArray,a11HeaderDataType: A11HeaderDataType) : ByteArray{
        var a11Packet = __protocolHandler.a11TotalPacketHandler.decoding(a11DataBinary)
        //设置 A11 协议头 信息
        a11Packet.packetHeader.ruleType = 1
        a11Packet.packetHeader.manuId = 0x0007
        a11Packet.packetHeader.instrument = A11HeaderInstrumentType.HTSC.value.toInt().and(0xFFFF).toUInt().toString().toInt()
        a11Packet.packetHeader.groupId = 0
        a11Packet.packetHeader.sn = 0
        //传入设置
        a11Packet.packetHeader.dataType = a11HeaderDataType.value.toInt().and(0xFFFF).toUInt().toString().toInt()

        return __protocolHandler.a11TotalPacketHandler.coding(a11Packet)
    }


    //设置 frame data 部分  CF
    private  fun packageZigbeeCFPacket(a11TotalBinary : ByteArray,frameId :Byte) : ByteArray{

        //初始化 Zigbee CF Packet
        var zigbeeCFPacket = ApiZigbeeCFPacket()

        //添加 mcfg 到 zigbee 负载部分
        zigbeeCFPacket.dataPayload = a11TotalBinary

        //设置 zigbee Frame Data 部分
        //传入设置
        zigbeeCFPacket.frameId = 0x01 //无定义？

        zigbeeCFPacket.destNetAddr = __srcNetAddr16
        zigbeeCFPacket.destAddr = __srcAddr64

        zigbeeCFPacket.tranOpt = 0x00 // XbeeProSentOption_Null

        zigbeeCFPacket.srcEndpoint = 0xE8.toByte()
        zigbeeCFPacket.destEndpoint = 0xE8.toByte()

        zigbeeCFPacket.clusterId = 0x0011 //DefaultClusterID
        zigbeeCFPacket.profileId = 0x1857 //DefaultFileID
        zigbeeCFPacket.broadcastRadius = 0x00 //DefaultBroadcast

        //Zigbee Frame Data转 二进制
        return __protocolHandler.apiZigbeeCFPacketHandler.coding(zigbeeCFPacket)
    }

    //设置zigbee 完整帧信息
    private fun packageTotalZigbee(frameData: ByteArray) : ByteArray{
        //设置 ZigBee 完整帧 其他信息
        var zigBeePacket = ZigBeePacket()

        //设置添加frame Data
        var tempData = ByteArray(frameData.size)
        System.arraycopy(frameData,0,tempData,0,frameData.size)
        zigBeePacket.frameData = tempData

        //转二进制 添加头部 长度 和校验
        return __protocolHandler.zigBeePacketHandler.coding(zigBeePacket)
    }


    //心跳包
    private fun packageHeartBeat(){
        if(__connPairStep == 3){
            __connPairStep = 4
        }
        var a11MCFGCmdOneRsvOnePacket = MCFGCmdOneRsv1Packet()
        a11MCFGCmdOneRsvOnePacket.cmdOpt = MCFGCtHeaderCmdType.HeartBeat.value.toByte()
        var A11binaryData =  packageA11Part(__protocolHandler.mcfgCmdOneRsvOnePacketHandler
            .encoding(a11MCFGCmdOneRsvOnePacket),A11HeaderDataType.ParameterMcfgCt)
        var cfBinaryData = packageZigbeeCFPacket(A11binaryData,0x01)
        var zigbeeBinaryData = packageTotalZigbee(cfBinaryData)

        __serialHelper.write(zigbeeBinaryData)

//        Log.i(tagApp," <-- ProtocolData :HeartBeat :${zigbeeBinaryData.toHexString()}")
    }

    //退出配置
    private fun packageDisCfg(){
        var a11MCFGCmdOneRsvOnePacket = MCFGCmdOneRsv1Packet()
        a11MCFGCmdOneRsvOnePacket.cmdOpt = MCFGCtHeaderCmdType.Quit.value.toByte()
        var A11binaryData =  packageA11Part(__protocolHandler.mcfgCmdOneRsvOnePacketHandler
            .encoding(a11MCFGCmdOneRsvOnePacket),A11HeaderDataType.ParameterMcfgCt)
        var cfBinaryData = packageZigbeeCFPacket(A11binaryData,0x01)
        var zigbeeBinaryData = packageTotalZigbee(cfBinaryData)

        __serialHelper.write(zigbeeBinaryData)

//        Log.i(tagApp," <-- ProtocolData Dis Config :${zigbeeBinaryData.toHexString()}")
    }


    //发送信息
    private  fun serialWriteProtocolData(sendData : ByteArray){
        if(__isFistRevAPI_ZigbeeTS){
            __serialHelper.write(sendData)
//            Log.i(tagApp," <-- ProtocolData :${sendData.toHexString()}")
        }else{
            tmrHeartBeatStop()
            __serialHelper.write(sendData)
//            Log.i(tagApp," <-- ProtocolData :${sendData.toHexString()}")
            tmrHeartBeatStart()
        }
    }

    //心跳定时器启动
    private fun tmrHeartBeatStart(){
        tmrHeartBeatStop()
            __tmrHeartBeat = fixedRateTimer("", false, 5000, 8000) {
            packageHeartBeat()
        }
        Log.i(tagApp,"tmrHeartBeatStart")
    }

    //心跳定时器关闭
    private fun tmrHeartBeatStop(){
        __tmrHeartBeat?.cancel()
        __tmrHeartBeat?.purge()
        Log.i(tagApp,"tmrHeartBeatStop")
    }

    //重置数据
    private fun resetInitPara(){
        tmrHeartBeatStop()
        __isFistRevAPI_ZigbeeTS = true
        __srcNetAddr16 = 0
        __srcAddr64 = ByteArray(8)
    }

    //生成MCFG/CT cmd 1 byte ,保留 1 bytes 报文
    private fun packageMcfgCmdOneRsv1Packet(mcfgCtHeaderCmdType: MCFGCtHeaderCmdType,
                                            a11HeaderDataType: A11HeaderDataType): ByteArray {
        var mcfgCmdOneRsvOnePacket = MCFGCmdOneRsv1Packet()
        mcfgCmdOneRsvOnePacket.cmdOpt = mcfgCtHeaderCmdType.value.toByte()

        var a11binaryData = packageA11Part(
            __protocolHandler.mcfgCmdOneRsvOnePacketHandler.encoding(mcfgCmdOneRsvOnePacket),
            a11HeaderDataType
        )
        var cfBinaryData = packageZigbeeCFPacket(a11binaryData, 0x01)
        return packageTotalZigbee(cfBinaryData)
    }

    private fun packageMcfgCmdOneRsv1Packet(mcfgCtHeaderCmdType: MCFGCtHeaderCmdType,
                                            a11HeaderDataType: A11HeaderDataType,rsv:Byte): ByteArray {
        var mcfgCmdOneRsvOnePacket = MCFGCmdOneRsv1Packet()
        mcfgCmdOneRsvOnePacket.cmdOpt = mcfgCtHeaderCmdType.value.toByte()
        mcfgCmdOneRsvOnePacket.rsv1 = rsv

        var a11binaryData = packageA11Part(
            __protocolHandler.mcfgCmdOneRsvOnePacketHandler.encoding(mcfgCmdOneRsvOnePacket),
            a11HeaderDataType
        )
        var cfBinaryData = packageZigbeeCFPacket(a11binaryData, 0x01)
        return packageTotalZigbee(cfBinaryData)
    }


    private fun mcfgReadCmdMsgWithPara(mcfgCtHeaderCmdType: MCFGCtHeaderCmdType){
        //MCFGCtHeaderCmdType.BasicParamRead
        //A11HeaderDataType.ParameterMcfgCt
        Log.i(tagApp,"mcfgReadCmdMsg CmdType: ${mcfgCtHeaderCmdType.value.toByte().toString(16)}")
        var frameBinary =  packageMcfgCmdOneRsv1Packet(mcfgCtHeaderCmdType,A11HeaderDataType.ParameterMcfgCt)
//        Log.i(tagApp,"read ParameterMcfgCt Remote Package  :${frameBinary.toHexString()}")
        serialWriteProtocolData(frameBinary)
    }


    private fun mcfgReadCmdMsgWithCal(mcfgCtHeaderCmdType: MCFGCtHeaderCmdType,rsv: Byte ){
        //MCFGCtHeaderCmdType.BasicParamRead
        //A11HeaderDataType.ParameterMcfgCt
        Log.i(tagApp,"mcfgReadCmdMsg CmdType: ${mcfgCtHeaderCmdType.value.toByte().toString(16)}")
        var frameBinary =  packageMcfgCmdOneRsv1Packet(mcfgCtHeaderCmdType,A11HeaderDataType.CalibrationMcfgCt,rsv)
//        Log.i(tagApp,"read CalibrationMcfgCt Remote Package  :${frameBinary.toHexString()}")
        serialWriteProtocolData(frameBinary)
    }



    private fun mcfgWriteCmdMsg(mcfgCtPacket: McfgCtPacket,a11HeaderDataType:A11HeaderDataType){
        Log.i(tagApp,"mcfgWriteCmdMsg  :${mcfgCtPacket.cmdType.toString(16)}")

        var a11BinaryData = packageA11Part(
            __protocolHandler.mcfgCtPacketHandler.encoding(mcfgCtPacket),
            a11HeaderDataType
        )
        var cfBinaryData = packageZigbeeCFPacket(a11BinaryData, 0x01)
        var sendBinary =  packageTotalZigbee(cfBinaryData)
//        Log.i(tagApp,"mcfgWriteCmdMsg  :${sendBinary.toHexString()}")
        serialWriteProtocolData(sendBinary)
    }


    private fun dealA11WithCf(frameData : ByteArray){

    }


    companion object {
        private const val DEFAULT_POSITION = 1
    }


}