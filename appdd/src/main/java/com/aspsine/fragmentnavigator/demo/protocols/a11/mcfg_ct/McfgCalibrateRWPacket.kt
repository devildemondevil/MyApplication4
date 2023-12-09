package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.55 校准参数读应答命令数据帧格式
 * 注：命令类型为 0x11
 *
 * 表表 表 C.56 校准参数写请求命令数据帧格式
 * 注：命令类型为 0x12
 *
 * Bytes:54
 */

class McfgCalibrateRWPacket {

    /**
     * 帧头  Bytes:10  index:0~9
     */
    var packetHeader : A11PacketHeader = A11PacketHeader()

    /**
     * 命令类型  Bytes:1    index:10
     */
    var cmdOpt : Byte = 0

    /**
     * 标定点数   Bytes:1     index:11
     * 2~5
     */
    var calibratePointQuantity :Byte = 0

    /**
     * 传感器序号  Bytes:2    index:12~13
     */
    var sensorNum :Int = 0

    /**
     * 标定点1    Bytes:8   index:14~21
     */
    var caliPt1 :ByteArray = ByteArray(8)


    /**
     * 标定点2    Bytes:8   index:22~29
     */
    var caliPt2 :ByteArray = ByteArray(8)

    /**
     * 标定点3    Bytes:8   index:30~37
     */
    var caliPt3 :ByteArray = ByteArray(8)

    /**
     * 标定点4    Bytes:8   index:38~45
     */
    var caliPt4 :ByteArray = ByteArray(8)


    /**
     * 标定点5    Bytes:8   index:46~53
     */
    var caliPt5 :ByteArray = ByteArray(8)

}