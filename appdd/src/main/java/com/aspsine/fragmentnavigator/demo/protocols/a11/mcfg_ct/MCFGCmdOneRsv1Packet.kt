package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.45 链接请求命令数据帧格式           R
 * cmdOpt: 0x00
 *
 * 表 C.46 基本参数读请求命令数据帧格式      W
 * cmdOpt: 0x01
 *
 * 表 C.49 基本参数写应答命令数据帧格式      R
 * cmdOpt: 0x02
 *
 * 表 C.50 扩展参数读请求命令数据帧格式      W
 * cmdOpt: 0x03
 *
 * 表 C.53 扩展参数写应答命令数据帧格式      R
 * cmdOpt: 0x04
 *
 * 表 C.54 校准参数读请求命令数据帧格式      W
 * cmdOpt: 0x11
 *
 * 表 C.57 校准参数写应答命令数据帧格式      R
 * cmdOpt: 0x12
 *
 * 表 C.58 读取 AD 值请求命令数据帧格式     W
 * cmdOpt: 0x13
 *
 * 表 C.62 链路心跳命令数据帧格式           W
 * cmdOpt: 0xFE
 *
 * 表 C.63 退出配置与校准命令数据帧格式      W
 * cmdOpt: 0xFF
 *
 * Total Bytes:12
 *
 */

class MCFGCmdOneRsv1Packet {

    /**
     * 消息头  Bytes:10  index:0~9
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
}