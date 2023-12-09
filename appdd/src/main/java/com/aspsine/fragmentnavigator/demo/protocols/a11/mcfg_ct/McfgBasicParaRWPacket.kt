package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.47 基本参数读应答命令数据帧格式
 * 注：命令类型为 0x01
 *
 * 表 C.48 基本参数写请求命令数据帧格式
 * 注：命令类型为 0x02
 *
 * Bytes:84
 */

class McfgBasicParaRWPacket {

    /**
     * 帧头  Bytes:10  index:0~9
     */
    var packetHeader : A11PacketHeader = A11PacketHeader()

    /**
     * 命令类型  Bytes:1    index:10
     */
    var cmdOpt : Byte = 0

    /**
     * 保留   Bytes:1     index:11
     */
    var rsv1 :Byte = 0

    /**
     * 位置信息  Bytes:20      index:12~31
     * 命令模式下不可修改
     */
    var positionInfo:ByteArray = ByteArray(20)

    /**
     * PAN_ID 号      Bytes:2     index:32~33
     * 命令模式下不可修改
     */
    var panId :Int = 0

    /**
     * 通道号       Bytes:1         index:34
     * 命令模式下不可修改
     */
    var channelNo :Byte = 0


    /**
     * 仪表组号         Bytes:1     index:35
     */
    var devGroupNo :Byte = 0

    /**
     * 仪表编号     Bytes:1     index:36
     */
    var devNum:Byte = 0

    /**
     * 报警使能     Bytes:1     index:37
     */
    var alarmEnable :Byte = 0

    /**
     * 休眠时间     Bytes:2     index:38~39
     * 无符号整数，单位：s,10-3600
     */
    var sleepTimes :Int = 0

    /**
     * 固件版本     Bytes:2     index:40~41
     */
    var firmwareVer :Int = 0

    /**
     * 报警上限    Bytes:4   index:42~45
     * 整型数
     */
    var alarmHigh : Long = 0

    /**
     * 报警下限     Bytes:4     index:46~49
     */
    var alarmLow :Long = 0

    /**
     * 报警死区     Bytes:2     index:50~51
     * 整型   2 B   (0.00%)
     */

    var alarmDeadband :Int = 0

    /**
     * 电池更换日期   Bytes:6     index:52~57
     * BCD 码  YYYY_MM_DD   6 B
     */
    var batteryData :ByteArray = ByteArray(6)

    /**
     * 投用日期     Bytes:6    index:58~63
     * BCD 码  YYYY_MM_DD   6 B
     */
    var useDate :ByteArray = ByteArray(6)

    /**
     * 保留       Bytes:20     index:64~83
     */
    var rsv2 :ByteArray = ByteArray(20)

}