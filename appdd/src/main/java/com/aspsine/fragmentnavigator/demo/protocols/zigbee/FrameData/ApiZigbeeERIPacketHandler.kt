package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import java.util.*

class ApiZigbeeERIPacketHandler {
    /**
     * 编码
     *
     * @param zigBeePacket
     * @return
     */
    fun encoding(apiZigbeeERIPacket: ApiZigbeeERIPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //Frame Type
        byteList.add(apiZigbeeERIPacket.frameType)

        //Source Address
        var srcAddr = apiZigbeeERIPacket.srcAddr.toMutableList()
        if(srcAddr.size !=8){
            //长度大于8  截断
            if(srcAddr.size > 8){
                srcAddr.slice(IntRange(0,7))
            }else{//小于8  补零
                var startIndex = srcAddr.size-1
                for( i in startIndex until 8){
                    srcAddr.add(0x00)
                }
            }
        }
        for(srcByte in srcAddr){
            byteList.add(srcByte)
        }

        //Source Network Address
        byteList.add((apiZigbeeERIPacket.srcNetAddr shr 8).toByte())//MSB
        byteList.add((apiZigbeeERIPacket.srcNetAddr and 0xFF).toByte())//LSB

        //Source Endpoint
        byteList.add(apiZigbeeERIPacket.srcEndpoint)

        //Destination Endpoint
        byteList.add(apiZigbeeERIPacket.destEndpoint)

        //Cluster ID
        byteList.add((apiZigbeeERIPacket.clusterId shr 8).and(0xFF).toByte())
        byteList.add((apiZigbeeERIPacket.clusterId and 0xFF).toByte())

        //Profile ID
        byteList.add((apiZigbeeERIPacket.profileId shr 8).and(0xFF).toByte())
        byteList.add((apiZigbeeERIPacket.profileId and 0xFF).toByte())

        //Receive Options
        byteList.add(apiZigbeeERIPacket.recvOpt)

        //Received Data
        var revData = apiZigbeeERIPacket.recvData
        for(revByte in revData){
            byteList.add(revByte)
        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param zigBeePacketBinary
     * @return
     */
    fun decoding(zigBeePacketBinary: ByteArray): ApiZigbeeERIPacket {
        val apiZigbeeERIPacket = ApiZigbeeERIPacket()

        //Frame Type       Bytes:1  index:0
        apiZigbeeERIPacket.frameType = zigBeePacketBinary[0]

        //Source Address  MSB~LSB  Bytes:8  index:1~8
        var tempSrcAddr = ByteArray(8)
        System.arraycopy(zigBeePacketBinary,1,tempSrcAddr,0,8)
        apiZigbeeERIPacket.srcAddr = tempSrcAddr

        //Source Network Address  MSB~LSB  Bytes:2  index:9~10
        var srcNetAddr :Int = zigBeePacketBinary[9].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[10].toInt().and(0xFF))
            .and(0xFFFF)
        apiZigbeeERIPacket.srcNetAddr = srcNetAddr

        //Source Endpoint   Bytes:1    index:11
        apiZigbeeERIPacket.srcEndpoint = zigBeePacketBinary[11]

        //Destination Endpoint  Bytes:1  index:12
        apiZigbeeERIPacket.destEndpoint = zigBeePacketBinary[12]

        //Cluster ID    Bytes: 2  index:13~14
        var clusterId :Int = zigBeePacketBinary[13].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[14].toInt().and(0xFF))
            .and(0xFFFF)
        apiZigbeeERIPacket.clusterId = clusterId

        //Profile ID    Bytes:2  index:15~16
        var profileId :Int = zigBeePacketBinary[15].toInt().and(0xFF).shl(8)
            .or(zigBeePacketBinary[16].toInt().and(0xFF))
            .and(0xFFFF)
        apiZigbeeERIPacket.profileId = profileId

        //Receive Options    Bytes:1  index:17
        apiZigbeeERIPacket.recvOpt = zigBeePacketBinary[17]

        //Received Data  Byte:剩余  index:18~end
        var recvDataSize : Int = zigBeePacketBinary.size - 18
        var tempRecvData = ByteArray(recvDataSize)
        System.arraycopy(zigBeePacketBinary,18,tempRecvData,0,recvDataSize)
        apiZigbeeERIPacket.recvData = tempRecvData

        return apiZigbeeERIPacket
    }

}