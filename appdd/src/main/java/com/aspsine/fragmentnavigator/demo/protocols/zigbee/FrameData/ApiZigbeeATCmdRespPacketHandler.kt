package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import java.util.*

class ApiZigbeeATCmdRespPacketHandler {
    /**
     * 编码
     *
     * @param apiZigbeeATCmdRespPacket
     * @return
     */
    fun encoding(apiZigbeeATCmdRespPacket: ApiZigbeeATCmdRespPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //Frame Type       Bytes:1  index:0
        byteList.add(apiZigbeeATCmdRespPacket.frameType)

        //Frame Id        Bytes:1  index:1
        byteList.add(apiZigbeeATCmdRespPacket.frameId)

        //AT Command     Bytes:2   index:2~3   String ==> Byte 2
        var atcmds = apiZigbeeATCmdRespPacket.atCmd.toByteArray().toMutableList()
        if(atcmds.size > 2){
            atcmds.slice(IntRange(0,1))
        }

        for(cmdByte in atcmds){
            byteList.add(cmdByte)
        }

        //Command Status   Bytes:1  index:4
        byteList.add(apiZigbeeATCmdRespPacket.cmdStatus)

        //Parameter Value  Byte:0~256  optional  index:5~End

        if(apiZigbeeATCmdRespPacket.isParasValueInitialzed()){
            var paras = apiZigbeeATCmdRespPacket.cmdData!!.toMutableList()

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
    fun decoding(zigBeePacketBinary: ByteArray): ApiZigbeeATCmdRespPacket {

        val apiZigbeeATCmdRespPacket = ApiZigbeeATCmdRespPacket()

        //长度不足
        if(zigBeePacketBinary.size < 5)
            return  apiZigbeeATCmdRespPacket

//         Frame Type       Bytes:1  index:0
        apiZigbeeATCmdRespPacket.frameType = zigBeePacketBinary[0]

//         Frame Id        Bytes:1  index:1
        apiZigbeeATCmdRespPacket.frameId = zigBeePacketBinary[1]

//         AT Command     Bytes:2   index:2~3   String ==> Byte 2
        var cmdByteArray = zigBeePacketBinary.sliceArray(IntRange(2,3))
        apiZigbeeATCmdRespPacket.atCmd = String(cmdByteArray)

//         Command Status   Bytes:1  index:4
        apiZigbeeATCmdRespPacket.cmdStatus = zigBeePacketBinary[4]

//         Parameter Value  Byte:0~256  optional  index:5~End
        var parasData  = ByteArray(0)

        if(zigBeePacketBinary.size - 5 > 0 ){
            parasData = zigBeePacketBinary.sliceArray(IntRange(5,zigBeePacketBinary.size-1))

            var parasSize = parasData.size

            if(parasSize > 0){
                if(parasSize > 256){
                    parasData.slice(IntRange(0,255))
                }
                apiZigbeeATCmdRespPacket.cmdData = parasData
            }
        }else{
            apiZigbeeATCmdRespPacket.cmdData = ByteArray(0)
        }



        return apiZigbeeATCmdRespPacket
    }

}