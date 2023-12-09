package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler

class McfgCmdOneRsv21PacketHandler {
    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param mcfgCmdOneRsv21Packet
     * @return
     */
    fun encoding(mcfgCmdOneRsv21Packet: McfgCmdOneRsv21Packet): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //消息头  Bytes:10  index:0~9
        var headListBinary = a11PacketHeaderHandler.coding(mcfgCmdOneRsv21Packet.packetHeader)
        for(headByte in headListBinary){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(mcfgCmdOneRsv21Packet.cmdOpt)

        //保留   Bytes:21     index:11~31
        var tempRsv = mcfgCmdOneRsv21Packet.rsv.toMutableList()
        //小于21 补零
        if(tempRsv.size < 21){
            var startIndex = tempRsv.size -1
            for(i in startIndex until 21){
                tempRsv.add(0x00)
            }
        }else if(tempRsv.size > 21){//大于 21 截断
            tempRsv.slice(IntRange(0,20))
        }
        for(rsvByte in tempRsv){
            byteList.add(rsvByte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param mcfgCmdOneRsv21Binary
     * @return
     */
    fun decoding(mcfgCmdOneRsv21Binary: ByteArray): McfgCmdOneRsv21Packet {

        val framePacket = McfgCmdOneRsv21Packet()

        //消息头  Bytes:10  index:0~9
        var tempHeader = ByteArray(10)
        System.arraycopy(mcfgCmdOneRsv21Binary,0,tempHeader,0,10)
        framePacket.packetHeader = a11PacketHeaderHandler.decoding(tempHeader)

        //命令类型  Bytes:1    index:10
        framePacket.cmdOpt = mcfgCmdOneRsv21Binary[10]

        //保留   Bytes:21     index:11~31
        //从11到最后
        //剩余大小
        var rsvSize = mcfgCmdOneRsv21Binary.size - 11

        var rsvData = ByteArray(21)

        if (rsvSize in 1..21){
            System.arraycopy(mcfgCmdOneRsv21Binary,11,rsvData,0,rsvSize)
        }else if(rsvSize > 21){
            System.arraycopy(mcfgCmdOneRsv21Binary,11,rsvData,0,21)
        }

        framePacket.rsv = rsvData

        return framePacket
    }

}