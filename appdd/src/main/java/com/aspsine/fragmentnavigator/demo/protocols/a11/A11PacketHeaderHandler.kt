package com.aspsine.fragmentnavigator.demo.protocols.a11

import java.util.*

class A11PacketHeaderHandler {
    /**
     * 编码
     *
     * @param a11PacketHeader
     * @return
     */
    fun coding(a11PacketHeader: A11PacketHeader): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //协议类型
        byteList.add((a11PacketHeader.ruleType shr 8).toByte())
        byteList.add((a11PacketHeader.ruleType and 0xFF).toByte())

        //厂商代码
        byteList.add((a11PacketHeader.manuId shr 8).toByte())
        byteList.add((a11PacketHeader.manuId and 0xFF).toByte())

        //仪表类型
        byteList.add((a11PacketHeader.instrument shr 8).toByte())
        byteList.add((a11PacketHeader.instrument and 0xFF).toByte())

        //仪表组号
        byteList.add(a11PacketHeader.groupId)

        //仪表编号
        byteList.add(a11PacketHeader.sn)

        //数据类型
        byteList.add((a11PacketHeader.dataType shr 8).toByte())
        byteList.add((a11PacketHeader.dataType and 0xFF).toByte())

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param a11HeaderBinary
     * @return
     */
    fun decoding(a11HeaderBinary: ByteArray): A11PacketHeader {

        val a11GrmPacketHeader = A11PacketHeader()

        //协议类型
        a11GrmPacketHeader.ruleType = a11HeaderBinary[0].toInt().and(0xFF).shl(8).and(0xFFFF)
            .or(a11HeaderBinary[1].toInt().and(0xFF)).and(0xFFFF)

        //厂商代码
        a11GrmPacketHeader.manuId = a11HeaderBinary[2].toInt().and(0xFF).shl(8).and(0xFFFF)
            .or(a11HeaderBinary[3].toInt().and(0xFF)).and(0xFFFF)

        //仪表类型
        a11GrmPacketHeader.instrument = a11HeaderBinary[4].toInt().and(0xFF).shl(8).and(0xFFFF)
            .or(a11HeaderBinary[5].toInt().and(0xFF)).and(0xFFFF)

        //仪表组号
        a11GrmPacketHeader.groupId = a11HeaderBinary[6]

        //仪表编号
        a11GrmPacketHeader.sn = a11HeaderBinary[7]

        //数据类型
        a11GrmPacketHeader.dataType = a11HeaderBinary[8].toInt().and(0xFF).and(0xFFFF).shl(8)
            .or(a11HeaderBinary[9].toInt().and(0xFF)).and(0xFFFF)

        return a11GrmPacketHeader
    }

}