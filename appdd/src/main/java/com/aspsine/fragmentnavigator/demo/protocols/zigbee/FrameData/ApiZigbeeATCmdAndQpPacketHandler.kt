package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import java.util.*

class ApiZigbeeATCmdAndQpPacketHandler {
    /**
     * 编码
     *
     * @param apiZigbeeATCmdAndQpPacket
     * @return
     */
    fun encoding(apiZigbeeATCmdAndQpPacket: ApiZigbeeATCmdAndQpPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //Frame Type       Bytes:1  index:0
        byteList.add(apiZigbeeATCmdAndQpPacket.frameType)

        //Frame Id        Bytes:1  index:1
        byteList.add(apiZigbeeATCmdAndQpPacket.frameId)

        //AT Command     Bytes:2   index:2~3   String ==> Byte 2
        var atcmds = apiZigbeeATCmdAndQpPacket.atCmd.toByteArray().toMutableList()
        if(atcmds.size > 2){
            atcmds.slice(IntRange(0,1))
        }

        for(cmdByte in atcmds){
            byteList.add(cmdByte)
        }

        //Parameter Value  Byte:0~256  optional  index:4~End

        if(apiZigbeeATCmdAndQpPacket.isParasValueInitialzed()){
            var paras = apiZigbeeATCmdAndQpPacket.parasValues!!.toMutableList()

            if(paras.size > 256){
                paras.slice(IntRange(0,255))
            }

            if(paras.size > 0 ){
                for(dataByte in paras){
                    byteList.add(dataByte)
                }
            }

        }

        return byteList.toByteArray()

    }

    /**
     * 解码
     *
     * @param zigBeePacketBinary
     * @return
     */
    fun decoding(zigBeePacketBinary: ByteArray): ApiZigbeeATCmdAndQpPacket {

        val apiZigbeeATCmdPacket = ApiZigbeeATCmdAndQpPacket()

        //长度不足
        if(zigBeePacketBinary.size < 4)
            return  apiZigbeeATCmdPacket

//         Frame Type       Bytes:1  index:0
        apiZigbeeATCmdPacket.frameType = zigBeePacketBinary[0]

//         Frame Id        Bytes:1  index:1
        apiZigbeeATCmdPacket.frameId = zigBeePacketBinary[1]

//         AT Command     Bytes:2   index:2~3   String ==> Byte 2
        var cmdByteArray = zigBeePacketBinary.sliceArray(IntRange(2,3))
        apiZigbeeATCmdPacket.atCmd = String(cmdByteArray)

//         Parameter Value  Byte:0~256  optional  index:4~End
        var parasData  = ByteArray(0)

        if(zigBeePacketBinary.size - 4 > 0 ){
            parasData = zigBeePacketBinary.sliceArray(IntRange(4,zigBeePacketBinary.size-1))
        }

        var parasSize = parasData.size

        if(parasSize > 0){
            if(parasSize > 256){
                parasData.slice(IntRange(0,255))
            }
            apiZigbeeATCmdPacket.parasValues = parasData
        }

        return apiZigbeeATCmdPacket
    }

}