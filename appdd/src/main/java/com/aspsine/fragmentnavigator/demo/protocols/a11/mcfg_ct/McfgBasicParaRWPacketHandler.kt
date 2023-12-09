package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler

class McfgBasicParaRWPacketHandler {

    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param mcfgBasicParaRWPacket
     * @return
     */
    fun encoding(mcfgBasicParaRWPacket: McfgBasicParaRWPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //帧头  Bytes:10  index:0~9
        var a11Header = a11PacketHeaderHandler.coding(mcfgBasicParaRWPacket.packetHeader)
        for(headByte in a11Header){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(mcfgBasicParaRWPacket.cmdOpt)

        //保留   Bytes:1     index:11
        byteList.add(mcfgBasicParaRWPacket.rsv1)

        //位置信息  Bytes:20      index:12~31
        var tempPosition = mcfgBasicParaRWPacket.positionInfo
        for(positionByte in tempPosition){
            byteList.add(positionByte)
        }

        //PAN_ID 号      Bytes:2     index:32~33
        byteList.add((mcfgBasicParaRWPacket.panId shr 8).and(0xFF).toByte())//MSB
        byteList.add((mcfgBasicParaRWPacket.panId and 0xFF).and(0xFF).toByte())//LSB

        //通道号       Bytes:1         index:34
        byteList.add(mcfgBasicParaRWPacket.channelNo)

        //仪表组号         Bytes:1     index:35
        byteList.add(mcfgBasicParaRWPacket.devGroupNo)

        //仪表编号     Bytes:1     index:36
        byteList.add(mcfgBasicParaRWPacket.devNum)

        //报警使能     Bytes:1     index:37
        byteList.add(mcfgBasicParaRWPacket.alarmEnable)

        //休眠时间     Bytes:2     index:38~39
        byteList.add((mcfgBasicParaRWPacket.sleepTimes shr 8).and(0xFF).toByte())//MSB
        byteList.add((mcfgBasicParaRWPacket.sleepTimes and 0xFF).and(0xFF).toByte())//LSB

        //固件版本     Bytes:2     index:40~41
        byteList.add((mcfgBasicParaRWPacket.firmwareVer shr 8).and(0xFF).toByte())//MSB
        byteList.add((mcfgBasicParaRWPacket.firmwareVer and 0xFF).and(0xFF).toByte())//LSB

        //报警上限    Bytes:4   index:42~45
        byteList.add(mcfgBasicParaRWPacket.alarmHigh.shr(24).and(0xFF).toByte())
        byteList.add(mcfgBasicParaRWPacket.alarmHigh.shr(16).and(0xFF).toByte())
        byteList.add(mcfgBasicParaRWPacket.alarmHigh.shr(8).and(0xFF).toByte())
        byteList.add(mcfgBasicParaRWPacket.alarmHigh.and(0xFF).toByte())

        //报警下限     Bytes:4     index:46~49
        byteList.add(mcfgBasicParaRWPacket.alarmLow.shr(24).and(0xFF).toByte())
        byteList.add(mcfgBasicParaRWPacket.alarmLow.shr(16).and(0xFF).toByte())
        byteList.add(mcfgBasicParaRWPacket.alarmLow.shr(8).and(0xFF).toByte())
        byteList.add(mcfgBasicParaRWPacket.alarmLow.and(0xFF).toByte())

        //报警死区     Bytes:2     index:50~51
        byteList.add((mcfgBasicParaRWPacket.alarmDeadband shr 8).and(0xFF).toByte())//MSB
        byteList.add((mcfgBasicParaRWPacket.alarmDeadband and 0xFF).and(0xFF).toByte())//LSB

        //电池更换日期   Bytes:6     index:52~57
        var tempBatteryData = mcfgBasicParaRWPacket.batteryData
        for(byte in tempBatteryData){
            byteList.add(byte)
        }

        //投用日期     Bytes:6    index:58~63
        var tempUseDate = mcfgBasicParaRWPacket.useDate
        for(byte in tempUseDate){
            byteList.add(byte)
        }

        //保留       Bytes:20     index:64~83
        var tempRsv2 = mcfgBasicParaRWPacket.rsv2
        for(byte in tempRsv2){
            byteList.add(byte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param mcfgBasicParaRWBinary
     * @return
     */
    fun decoding(mcfgBasicParaRWBinary: ByteArray): McfgBasicParaRWPacket {


        val packet = McfgBasicParaRWPacket()

        if(mcfgBasicParaRWBinary.size < 84)
            return packet

        //帧头  Bytes:10  index:0~9
        packet.packetHeader = a11PacketHeaderHandler.decoding(mcfgBasicParaRWBinary)

        //命令类型  Bytes:1    index:10
        packet.cmdOpt = mcfgBasicParaRWBinary[10]

        //保留   Bytes:1     index:11
        packet.rsv1 = mcfgBasicParaRWBinary[11]

        //位置信息  Bytes:20      index:12~31
        var tempPosition = ByteArray(20)
        System.arraycopy(mcfgBasicParaRWBinary,12,tempPosition,0,20)
        packet.positionInfo = tempPosition

        //PAN_ID 号      Bytes:2     index:32~33
        packet.panId = mcfgBasicParaRWBinary[32].toInt().and(0xFF).shl(8)
            .or(mcfgBasicParaRWBinary[33].toInt().and(0xFF))
            .and(0xFFFF)

        //通道号       Bytes:1         index:34
        packet.channelNo = mcfgBasicParaRWBinary[34]

        //仪表组号         Bytes:1     index:35
        packet.devGroupNo = mcfgBasicParaRWBinary[35]

        //仪表编号     Bytes:1     index:36
        packet.devNum = mcfgBasicParaRWBinary[36]

        //报警使能     Bytes:1     index:37
        packet.alarmEnable = mcfgBasicParaRWBinary[37]

        //休眠时间     Bytes:2     index:38~39
        packet.sleepTimes = mcfgBasicParaRWBinary[38].toInt().and(0xFF).shl(8)
            .or(mcfgBasicParaRWBinary[39].toInt().and(0xFF))
            .and(0xFFFF)

        //固件版本     Bytes:2     index:40~41
        packet.firmwareVer = mcfgBasicParaRWBinary[40].toInt().and(0xFF).shl(8)
            .or(mcfgBasicParaRWBinary[41].toInt().and(0xFF))
            .and(0xFFFF)

        //报警上限    Bytes:4   index:42~45
        packet.alarmHigh = mcfgBasicParaRWBinary[42].toLong().and(0xFF).shl(24)
            .or(mcfgBasicParaRWBinary[43].toLong().and(0xFF).shl(16))
            .or(mcfgBasicParaRWBinary[44].toLong().and(0xFF).shl(8))
            .or(mcfgBasicParaRWBinary[45].toLong().and(0xFF))
            .and(0xFFFFFFFF)

        //报警下限     Bytes:4     index:46~49
        packet.alarmLow = mcfgBasicParaRWBinary[46].toLong().shl(24)
            .or(mcfgBasicParaRWBinary[47].toLong().and(0xFF).shl(16))
            .or(mcfgBasicParaRWBinary[48].toLong().and(0xFF).shl(8))
            .or(mcfgBasicParaRWBinary[49].toLong()).and(0xFF)
            .and(0xFFFFFFFF)

        //报警死区     Bytes:2     index:50~51
        packet.alarmDeadband = mcfgBasicParaRWBinary[50].toInt().and(0xFF).shl(8)
            .or(mcfgBasicParaRWBinary[51].toInt().and(0xFF))
            .and(0xFFFF)

        //电池更换日期   Bytes:6     index:52~57
        var tempBattery = ByteArray(6)
        System.arraycopy(mcfgBasicParaRWBinary,52,tempBattery,0,6)
        packet.batteryData = tempBattery

        //投用日期     Bytes:6    index:58~63
        var tempUse = ByteArray(6)
        System.arraycopy(mcfgBasicParaRWBinary,58,tempUse,0,6)
        packet.useDate = tempUse

        //保留       Bytes:20     index:64~83
        var tempRsv = ByteArray(20)
        System.arraycopy(mcfgBasicParaRWBinary,64,tempRsv,0,20)
        packet.rsv2 = tempRsv

        return packet
    }

}