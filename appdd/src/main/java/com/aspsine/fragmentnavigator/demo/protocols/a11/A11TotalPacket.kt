package com.aspsine.fragmentnavigator.demo.protocols.a11

/**
 * 表 C.1 数据帧头定义 Header + data
 */
class A11TotalPacket {

    /**
     * 消息头  Bytes:10  index:0~9
     */
    var packetHeader : A11PacketHeader = A11PacketHeader()

    /**
     * 数据部分  Bytes:size - 10    index:10~End
     */
    lateinit var dataPart:ByteArray

}