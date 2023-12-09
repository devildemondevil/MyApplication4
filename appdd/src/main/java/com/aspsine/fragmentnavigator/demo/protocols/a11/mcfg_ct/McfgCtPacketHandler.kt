package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler
import java.util.*

class McfgCtPacketHandler {

    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()

    /**
     * 编码
     *
     * @param mcfgCtPacket
     * @return
     */
    fun encoding(mcfgCtPacket: McfgCtPacket?): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //消息头  Bytes:10  index:0~9
        var a11Header = a11PacketHeaderHandler.coding(mcfgCtPacket!!.a11packetHeader)
        for(headByte in a11Header){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(mcfgCtPacket.cmdType)

        //后续数据 剩余数据 index:11~End
        val flowData = mcfgCtPacket.FollowUpData
        for (dataByte in flowData) {
            byteList.add(dataByte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param mcfgCtPacketHeaderBinary
     * @return
     */
    fun decoding(mcfgCtPacketHeaderBinary: ByteArray): McfgCtPacket {
        val packet = McfgCtPacket()

        //消息头  Bytes:10  index:0~9
        var tempHeader = ByteArray(10)
        System.arraycopy(mcfgCtPacketHeaderBinary,0,tempHeader,0,10)
        packet.a11packetHeader = a11PacketHeaderHandler.decoding(tempHeader)

        //命令类型  Bytes:1    index:10
        packet.cmdType = mcfgCtPacketHeaderBinary[10]

        //后续数据 剩余数据 index:11~End
        var flowLength = if(mcfgCtPacketHeaderBinary.size >= 11 ) (mcfgCtPacketHeaderBinary.size - 11) else 0
        val tempFlow = ByteArray(flowLength)
        System.arraycopy(mcfgCtPacketHeaderBinary, 11, tempFlow, 0, flowLength)
        packet.FollowUpData = tempFlow

        return packet
    }

}