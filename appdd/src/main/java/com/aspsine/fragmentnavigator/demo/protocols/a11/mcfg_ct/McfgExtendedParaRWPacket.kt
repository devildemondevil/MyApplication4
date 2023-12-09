package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.51 扩展参数读应答命令数据帧格式
 * 注：命令类型为 0x03
 *
 * 表表 C.52 扩展参数写请求命令数据帧格式
 * 注：命令类型为 0x04
 *
 * Bytes:75
 */

class McfgExtendedParaRWPacket {

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
     * 功率等级  Bytes:1    index:12
     */
    var powerLevel :Byte = 0

    /**
     * 加密使能  Bytes:1    index:13
     *  0:不加密
     *  1:加密
     */
    var encryptEnable : Byte = 0

    /**
     * 加密选项   Bytes:1   index:14
     * 0x01:在入网期间获取/发送安全密钥
     * 0x02:使用“信任中心”（仅限于协调器）
     */
    var encryptOpt :Byte = 0x01

    /**
     * 连接密钥    Bytes:16   index:15~30
     * 默认为  0x11
     */
    var connKey :ByteArray = ByteArray(16)

    /**
     * 网络密钥   Bytes:16   index:31~46    MSB~LSB
     */
    var netKey :ByteArray = ByteArray(16)


    /**
     * 64位目标地址  MSB~LSB  Bytes:8  index:47~54
     */
    var destAddr :ByteArray = ByteArray(8)

    /**
     * 保留       Bytes:20     index:55~74
     */
    var rsv2 :ByteArray = ByteArray(20)

}