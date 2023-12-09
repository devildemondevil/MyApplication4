package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import java.util.*

class ApiZigbeeCFPacketHandler {
    /**
     * 编码
     *
     * @param zigBeePacket
     * @return
     */
    fun coding(apiZigbeeCFPacket: ApiZigbeeCFPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //Frame Type       Bytes:1  index:0
        byteList.add(apiZigbeeCFPacket.frameType)

        //Frame Id        Bytes:1  index:1
        byteList.add(apiZigbeeCFPacket.frameId)

        //64-bit Destination Address  MSB~LSB  Bytes:8  index:2~9
        var destAddr = apiZigbeeCFPacket.destAddr.toMutableList()
        if(destAddr.size !=8){
            //长度大于8  截断
            if(destAddr.size > 8){
                destAddr.slice(IntRange(0,7))
            }else{//小于8  补零
                var startIndex = destAddr.size-1
                for( i in startIndex until 8){
                    destAddr.add(0x00)
                }
            }
        }
        for(destByte in destAddr){
            byteList.add(destByte)
        }

        //16-bit Destination Network Address MSB~LSB  Bytes:2  index:10~11
        byteList.add((apiZigbeeCFPacket.destNetAddr shr 8).and(0xFF).toByte())//MSB
        byteList.add((apiZigbeeCFPacket.destNetAddr and 0xFF).and(0xFF).toByte())//LSB

        //Source Endpoint  Bytes:1    index:12
        byteList.add(apiZigbeeCFPacket.srcEndpoint)

        //Destination Endpoint Bytes:1  index:13
        byteList.add(apiZigbeeCFPacket.destEndpoint)

        //Cluster ID Bytes: 2  index:14~15
        byteList.add((apiZigbeeCFPacket.clusterId shr 8).and(0xFF).toByte())
        byteList.add((apiZigbeeCFPacket.clusterId and 0xFF).and(0xFF).toByte())

        //Profile ID Bytes:2  index:16~17
        byteList.add((apiZigbeeCFPacket.profileId shr 8).and(0xFF).toByte())
        byteList.add((apiZigbeeCFPacket.profileId and 0xFF).and(0xFF).toByte())

        //Broadcast Radius  Bytes:1  index:18
        byteList.add(apiZigbeeCFPacket.broadcastRadius)

        //Transmit Options Bytes:1  index:19
        byteList.add(apiZigbeeCFPacket.tranOpt)

        //Data Payload  Byte:?  index:19~End
        var dataPayload = apiZigbeeCFPacket.dataPayload
        for(dataByte in dataPayload){
            byteList.add(dataByte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param zigBeePacketBinary
     * @return
     */
    fun decoding(zigBeePacketBinary: ByteArray): ApiZigbeeCFPacket {

        val apiZigbeeCFPacket = ApiZigbeeCFPacket()

        //长度不足
        if(zigBeePacketBinary.size < 20)
            return  apiZigbeeCFPacket

//         Frame Type       Bytes:1  index:0
        apiZigbeeCFPacket.frameType = zigBeePacketBinary[0]

//         Frame Id        Bytes:1  index:1
        apiZigbeeCFPacket.frameId = zigBeePacketBinary[1]

//         64-bit Destination Address  MSB~LSB  Bytes:8  index:2~9
        var tempDestAddr = ByteArray(8)
        if(zigBeePacketBinary.size -1 > 8){
            System.arraycopy(zigBeePacketBinary,1,tempDestAddr,0,8)
        }
        apiZigbeeCFPacket.destAddr = tempDestAddr

//         16-bit Destination Network Address MSB~LSB  Bytes:2  index:10~11
        var destNetAddr :Int = zigBeePacketBinary[10].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[11].toInt().and(0xFF))
            .and(0xFFFF)
        apiZigbeeCFPacket.destNetAddr = destNetAddr

//         Source Endpoint  Bytes:1    index:12
        apiZigbeeCFPacket.srcEndpoint = zigBeePacketBinary[12]

//         Destination Endpoint Bytes:1  index:13
        apiZigbeeCFPacket.destEndpoint = zigBeePacketBinary[13]

//         Cluster ID Bytes: 2  index:14~15
        var clusterId :Int = zigBeePacketBinary[14].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[15].toInt().and(0xFF))
            .and(0xFFFF)
        apiZigbeeCFPacket.clusterId = clusterId

//         Profile ID Bytes:2  index:16~17
        var profileId :Int = zigBeePacketBinary[16].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[17].toInt().and(0xFF))
            .and(0xFFFF)
        apiZigbeeCFPacket.profileId = profileId

//         Broadcast Radius  Bytes:1  index:18
        apiZigbeeCFPacket.broadcastRadius = zigBeePacketBinary[18]

//         Transmit Options Bytes:1  index:19
        apiZigbeeCFPacket.tranOpt = zigBeePacketBinary[19]

//        Data Payload  Byte:?  index:19~End
        var payloadSize = zigBeePacketBinary.size - 19
        var payloadData :ByteArray
        if(payloadSize > 0){
            payloadData = ByteArray(payloadSize)
            System.arraycopy(zigBeePacketBinary,19,payloadData,0,payloadSize)
        }else{
            payloadData = ByteArray(0)
        }
        apiZigbeeCFPacket.dataPayload = payloadData

        return apiZigbeeCFPacket
    }

}