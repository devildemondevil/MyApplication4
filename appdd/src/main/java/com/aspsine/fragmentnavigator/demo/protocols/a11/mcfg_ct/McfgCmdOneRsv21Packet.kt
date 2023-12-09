package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.60 管理模式应答命令数据帧格式
 * cmdOpt: 0xF0  管理模式
 *
 * 表 C.61 命令模式命令数据帧格式
 * cmdOpt: 0xF1  命令模式
 *
 * Bytes:32
 *
 */

class McfgCmdOneRsv21Packet {

    /**
     * 消息头  Bytes:10  index:0~9
     */
    var packetHeader : A11PacketHeader = A11PacketHeader()

    /**
     * 命令类型  Bytes:1    index:10
     */
    var cmdOpt : Byte = 0

    /**
     * 保留   Bytes:21     index:11~31
     */
    var rsv :ByteArray = ByteArray(21)
}