package com.aspsine.fragmentnavigator.demo.protocols.a11

import java.util.*

class A11TotalPacketHandler {
    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param a11TotalPacket
     * @return
     */
    fun coding(a11TotalPacket: A11TotalPacket): ByteArray {

        val byteList: MutableList<Byte> = ArrayList()

        //消息头  Bytes:10  index:0~9
        var a11Header = a11PacketHeaderHandler.coding(a11TotalPacket.packetHeader)
        for(headByte in a11Header){
            byteList.add(headByte)
        }

        //数据部分 index:10~End
        val data = a11TotalPacket.dataPart
        for (dataByte in data) {
            byteList.add(dataByte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param a11TotalPacketBinary
     * @return
     */
    fun decoding(a11TotalPacketBinary: ByteArray): A11TotalPacket {
        val packet = A11TotalPacket()

        //消息头  Bytes:10  index:0~9
        var tempHeader = ByteArray(10)
        System.arraycopy(a11TotalPacketBinary,0,tempHeader,0,10)
        packet.packetHeader = a11PacketHeaderHandler.decoding(tempHeader)

        //后续数据 剩余数据 index:10~End
        var flowLength = if(a11TotalPacketBinary.size >= 10 ) (a11TotalPacketBinary.size - 10) else 0
        val tempFlow = ByteArray(flowLength)
        System.arraycopy(a11TotalPacketBinary, 10, tempFlow, 0, flowLength)
        packet.dataPart = tempFlow

        return packet
    }

}