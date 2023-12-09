package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler

class McfgCalibrateRWPacketHandler {

    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param mcfgCalibrateRWPacket
     * @return
     */
    fun encoding(mcfgCalibrateRWPacket: McfgCalibrateRWPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //帧头  Bytes:10  index:0~9
        var a11Header = a11PacketHeaderHandler.coding(mcfgCalibrateRWPacket.packetHeader)
        for(headByte in a11Header){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(mcfgCalibrateRWPacket.cmdOpt)

        //标定点数   Bytes:1     index:11
        byteList.add(mcfgCalibrateRWPacket.calibratePointQuantity)

        //传感器序号  Bytes:2    index:12~13
        byteList.add((mcfgCalibrateRWPacket.sensorNum shr 8).and(0xFF).toByte())//MSB
        byteList.add((mcfgCalibrateRWPacket.sensorNum and 0xFF).and(0xFF).toByte())//LSB


        //标定点1    Bytes:8   index:14~21
        var pt1 = mcfgCalibrateRWPacket.caliPt1.toMutableList()
        //小于8 补零
        if(pt1.size < 8){
            var startIndex = pt1.size -1
            for(i in startIndex until 8){
                pt1.add(0x00)
            }
        }else if(pt1.size > 8){//大于 16 截断
            pt1.slice(IntRange(0,8-1))
        }
        for(byte in pt1){
            byteList.add(byte)
        }

        //标定点2    Bytes:8   index:22~29
        var pt2 = mcfgCalibrateRWPacket.caliPt2.toMutableList()
        //小于8 补零
        if(pt2.size < 8){
            var startIndex = pt2.size -1
            for(i in startIndex until 8){
                pt2.add(0x00)
            }
        }else if(pt2.size > 8){//大于 16 截断
            pt2.slice(IntRange(0,8-1))
        }
        for(byte in pt2){
            byteList.add(byte)
        }

        //标定点3    Bytes:8   index:30~37
        var pt3 = mcfgCalibrateRWPacket.caliPt3.toMutableList()
        //小于8 补零
        if(pt3.size < 8){
            var startIndex = pt3.size -1
            for(i in startIndex until 8){
                pt3.add(0x00)
            }
        }else if(pt3.size > 8){//大于 16 截断
            pt3.slice(IntRange(0,8-1))
        }
        for(byte in pt3){
            byteList.add(byte)
        }

        //标定点4    Bytes:8   index:38~45
        var pt4 = mcfgCalibrateRWPacket.caliPt4.toMutableList()
        //小于8 补零
        if(pt4.size < 8){
            var startIndex = pt4.size -1
            for(i in startIndex until 8){
                pt4.add(0x00)
            }
        }else if(pt4.size > 8){//大于 16 截断
            pt4.slice(IntRange(0,8-1))
        }
        for(byte in pt4){
            byteList.add(byte)
        }

        //标定点5    Bytes:8   index:46~53
        var pt5 = mcfgCalibrateRWPacket.caliPt5.toMutableList()
        //小于8 补零
        if(pt5.size < 8){
            var startIndex = pt5.size -1
            for(i in startIndex until 8){
                pt5.add(0x00)
            }
        }else if(pt5.size > 8){//大于 16 截断
            pt5.slice(IntRange(0,8-1))
        }
        for(byte in pt5){
            byteList.add(byte)
        }


        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param mcfgBinary
     * @return
     */
    fun decoding(mcfgBinary: ByteArray): McfgCalibrateRWPacket {

        val packet = McfgCalibrateRWPacket()

        if(mcfgBinary.size < 54)
            return packet

        //帧头  Bytes:10  index:0~9
        packet.packetHeader = a11PacketHeaderHandler.decoding(mcfgBinary)

        //命令类型  Bytes:1    index:10
        packet.cmdOpt = mcfgBinary[10]

        //标定点数   Bytes:1     index:11
        packet.calibratePointQuantity = mcfgBinary[11]

        //传感器序号  Bytes:2    index:12~13
        packet.sensorNum = mcfgBinary[12].toInt().and(0xFF).shl(8)
            .or(mcfgBinary[13].toInt().and(0xFF))
            .and(0xFFFF)

        //标定点1    Bytes:8   index:14~21
        var pt1 = ByteArray(8)
        System.arraycopy(mcfgBinary,14,pt1,0,8)
        packet.caliPt1 = pt1

        //标定点2    Bytes:8   index:22~29
        var pt2 = ByteArray(8)
        System.arraycopy(mcfgBinary,22,pt2,0,8)
        packet.caliPt2 = pt2

        //标定点3    Bytes:8   index:30~37
        var pt3 = ByteArray(8)
        System.arraycopy(mcfgBinary,30,pt3,0,8)
        packet.caliPt3 = pt3

        //标定点4    Bytes:8   index:38~45
        var pt4 = ByteArray(8)
        System.arraycopy(mcfgBinary,38,pt4,0,8)
        packet.caliPt4 = pt4

        //标定点5    Bytes:8   index:46~53
        var pt5 = ByteArray(8)
        System.arraycopy(mcfgBinary,46,pt5,0,8)
        packet.caliPt5 = pt5

        return packet
    }

}