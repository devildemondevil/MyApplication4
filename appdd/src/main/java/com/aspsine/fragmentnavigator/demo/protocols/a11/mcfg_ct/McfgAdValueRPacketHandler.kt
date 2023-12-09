package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler

class McfgAdValueRPacketHandler {

    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param mcfgAdValueRPacket
     * @return
     */
    fun encoding(mcfgAdValueRPacket: McfgAdValueRPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //帧头  Bytes:10  index:0~9
        var a11Header = a11PacketHeaderHandler.coding(mcfgAdValueRPacket.packetHeader)
        for(headByte in a11Header){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(mcfgAdValueRPacket.cmdOpt)

        //保留   Bytes:1     index:11
        byteList.add(mcfgAdValueRPacket.rsv1)

        //传感器序号  Bytes:2      index:12~13
        byteList.add((mcfgAdValueRPacket.sensorNum shr 8).and(0xFF).toByte())//MSB
        byteList.add((mcfgAdValueRPacket.sensorNum and 0xFF).and(0xFF).toByte())//LSB

        //AD值     Bytes:8        index:14~21
        var adValue = mcfgAdValueRPacket.adValue
        for(byte in adValue){
            byteList.add(byte)
        }

        //保留       Bytes:20     index:64~83
        var tempRsv2 = mcfgAdValueRPacket.rsv2
        for(byte in tempRsv2){
            byteList.add(byte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param adValuePacketBinary
     * @return
     */
    fun decoding(adValuePacketBinary: ByteArray): McfgAdValueRPacket {


        val packet = McfgAdValueRPacket()

        if(adValuePacketBinary.size < 62)
            return packet

        //帧头  Bytes:10  index:0~9
        packet.packetHeader = a11PacketHeaderHandler.decoding(adValuePacketBinary)

        //命令类型  Bytes:1    index:10
        packet.cmdOpt = adValuePacketBinary[10]

        //保留   Bytes:1     index:11
        packet.rsv1 = adValuePacketBinary[11]

        //传感器序号  Bytes:2      index:12~13
        packet.sensorNum = adValuePacketBinary[12].toInt().and(0xFF).shl(8)
            .or(adValuePacketBinary[13].toInt().and(0xFF))
            .and(0xFFFF)

        //AD值     Bytes:8        index:14~21
        var adValue = ByteArray(8)
        System.arraycopy(adValuePacketBinary,14,adValue,0,8)
        packet.adValue = adValue

        //保留       Bytes:40     index:22~61
        var rsv2 = ByteArray(40)
        System.arraycopy(adValuePacketBinary,22,rsv2,0,40)
        packet.rsv2 = rsv2

        return packet
    }

}