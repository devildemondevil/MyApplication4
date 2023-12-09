package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler

class MCFGCmdOneRsv1PacketHandler {
    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param MCFGCmdOneRsv1Packet
     * @return
     */
    fun encoding(MCFGCmdOneRsv1Packet: MCFGCmdOneRsv1Packet): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //消息头  Bytes:10  index:0~9
        var headListBinary = a11PacketHeaderHandler.coding(MCFGCmdOneRsv1Packet.packetHeader)
        for(headByte in headListBinary){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(MCFGCmdOneRsv1Packet.cmdOpt)

        //保留   Bytes:1     index:11
        byteList.add(MCFGCmdOneRsv1Packet.rsv1)

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param a11MCFGHeartOrQuitBinary
     * @return
     */
    fun decoding(a11MCFGHeartOrQuitBinary: ByteArray): MCFGCmdOneRsv1Packet {

        val framePacket = MCFGCmdOneRsv1Packet()

        //消息头  Bytes:10  index:0~9
        var tempHeader = ByteArray(10)
        System.arraycopy(a11MCFGHeartOrQuitBinary,0,tempHeader,0,10)
        framePacket.packetHeader = a11PacketHeaderHandler.decoding(tempHeader)

        //命令类型  Bytes:1    index:10
        framePacket.cmdOpt = a11MCFGHeartOrQuitBinary[10]

        //保留   Bytes:1     index:11
        framePacket.cmdOpt = a11MCFGHeartOrQuitBinary[11]

        return framePacket
    }

}