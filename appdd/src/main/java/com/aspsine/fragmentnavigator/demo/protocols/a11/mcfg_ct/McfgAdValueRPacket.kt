package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.59 读取 AD 值应答命令数据帧格式
 * 命令类型为 0x13
 * Bytes:62
 */

class McfgAdValueRPacket {

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
     * 传感器序号  Bytes:2      index:12~13
     * 标准值 4B 浮点数，AD值 4B 整型
     */
    var sensorNum :Int = 0

    /**
     *  AD值     Bytes:8        index:14~21
     */
    var adValue :ByteArray = ByteArray(8)

    /**
     * 保留       Bytes:40     index:22~61
     */
    var rsv2 :ByteArray = ByteArray(20)

}