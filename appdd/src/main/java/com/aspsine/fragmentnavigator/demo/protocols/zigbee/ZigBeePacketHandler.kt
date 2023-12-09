package com.aspsine.fragmentnavigator.demo.protocols.zigbee

import java.util.*

class ZigBeePacketHandler {
    /**
     * 编码
     *
     * @param zigBeePacket
     * @return
     */
    fun coding(zigBeePacket: ZigBeePacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        byteList.add(zigBeePacket.startDelimiter.toByte())

        byteList.add((zigBeePacket.frameData.size shr 8).and(0xFF).toByte())
        byteList.add((zigBeePacket.frameData.size and 0xFF).toByte())

        val frameData = zigBeePacket.frameData
        for (dataByte in frameData) {
            byteList.add(dataByte)
        }

        byteList.add(checkSum(frameData))
        return byteList.toByteArray()
    }

    /**
     * 解码
     *
     * @param zigBeePacketBinary
     * @return
     */
    fun decoding(zigBeePacketBinary: ByteArray): ZigBeePacket {
        val zigBeePacket = ZigBeePacket()
        zigBeePacket.startDelimiter = zigBeePacketBinary[0]
        var length :Int = zigBeePacketBinary[1].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[2].toInt().and(0xFF))
            .and(0xFFFF)
        zigBeePacket.length = length
        val tempFrame = ByteArray(zigBeePacket.length)
        System.arraycopy(zigBeePacketBinary, 3, tempFrame, 0, zigBeePacket.length)
        zigBeePacket.frameData = tempFrame
        zigBeePacket.checkSum = zigBeePacketBinary[3 + zigBeePacket.length]

        zigBeePacket.apiId = zigBeePacket.frameData[0]
        val tempApiData = ByteArray(zigBeePacket.length-1)
        System.arraycopy(zigBeePacket.frameData, 1, tempApiData, 0, zigBeePacket.frameData.size - 1)
        zigBeePacket.apiData = tempApiData

        return zigBeePacket
    }

    /**
     * 校验和
     *
     * @param bytes
     * @return
     */
    fun checkSum(bytes: ByteArray): Byte {
        var checkSum :Byte =  bytes.sum().and(0xFF).toByte()
        return (0xFF - checkSum).toByte()
    }
}