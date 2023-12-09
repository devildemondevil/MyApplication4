package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler

class McfgExtendedParaRWPacketHandler {

    var a11PacketHeaderHandler : A11PacketHeaderHandler = A11PacketHeaderHandler()
    /**
     * 编码
     *
     * @param mcfgExtendedParaRWPacket
     * @return
     */
    fun encoding(mcfgExtendedParaRWPacket: McfgExtendedParaRWPacket): ByteArray {
        val byteList: MutableList<Byte> = ArrayList()

        //帧头  Bytes:10  index:0~9
        var a11Header = a11PacketHeaderHandler.coding(mcfgExtendedParaRWPacket.packetHeader)
        for(headByte in a11Header){
            byteList.add(headByte)
        }

        //命令类型  Bytes:1    index:10
        byteList.add(mcfgExtendedParaRWPacket.cmdOpt)

        //保留   Bytes:1     index:11
        byteList.add(mcfgExtendedParaRWPacket.rsv1)

        //功率等级  Bytes:1    index:12
        byteList.add(mcfgExtendedParaRWPacket.powerLevel)

        //加密使能  Bytes:1    index:13
        byteList.add(mcfgExtendedParaRWPacket.encryptEnable)

        //加密选项   Bytes:1   index:14
        byteList.add(mcfgExtendedParaRWPacket.encryptOpt)

        //连接密钥    Bytes:16   index:15~30
        var connKey = mcfgExtendedParaRWPacket.connKey.toMutableList()
        //小于16 补零
        if(connKey.size < 16){
            var startIndex = connKey.size -1
            for(i in startIndex until 16){
                connKey.add(0x00)
            }
        }else if(connKey.size > 16){//大于 16 截断
            connKey.slice(IntRange(0,16-1))
        }
        for(byte in connKey){
            byteList.add(byte)
        }

        //网络密钥   Bytes:16   index:31~46    MSB~LSB
        var netKey = mcfgExtendedParaRWPacket.netKey.toMutableList()
        //小于16 补零
        if(netKey.size < 16){
            var startIndex = netKey.size -1
            for(i in startIndex until 16){
                netKey.add(0x00)
            }
        }else if(netKey.size > 16){//大于 16 截断
            netKey.slice(IntRange(0,16-1))
        }
        for(byte in netKey){
            byteList.add(byte)
        }

        //64位目标地址  MSB~LSB  Bytes:8  index:47~54
        var destAddr = mcfgExtendedParaRWPacket.destAddr.toMutableList()
        //小于8 补零
        if(destAddr.size < 8){
            var startIndex = destAddr.size -1
            for(i in startIndex until 8){
                destAddr.add(0x00)
            }

        }else if(destAddr.size > 8){//大于 16 截断
            destAddr.slice(IntRange(0,8 -1 ))
        }
        for(byte in destAddr){
            byteList.add(byte)
        }

        //保留       Bytes:20     index:55~74
        var rsv2 = mcfgExtendedParaRWPacket.rsv2.toMutableList()
        //小于20 补零
        if(rsv2.size < 20){
            var startIndex = rsv2.size -1
            for(i in startIndex until 20){
                rsv2.add(0x00)
            }
        }else if(rsv2.size > 20){//大于 20 截断
            rsv2.slice(IntRange(0,20 - 1 ))
        }
        for(byte in rsv2){
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
    fun decoding(mcfgBinary: ByteArray): McfgExtendedParaRWPacket {

        val packet = McfgExtendedParaRWPacket()

        if(mcfgBinary.size < 75)
            return packet

        //帧头  Bytes:10  index:0~9
        packet.packetHeader = a11PacketHeaderHandler.decoding(mcfgBinary)

        //命令类型  Bytes:1    index:10
        packet.cmdOpt = mcfgBinary[10]

        //保留   Bytes:1     index:11
        packet.rsv1 = mcfgBinary[11]

        //功率等级  Bytes:1    index:12
        packet.powerLevel = mcfgBinary[12]

        //加密使能  Bytes:1    index:13
        packet.encryptEnable = mcfgBinary[13]

        //加密选项   Bytes:1   index:14
        packet.encryptOpt = mcfgBinary[14]

        //连接密钥    Bytes:16   index:15~30
        var connKey = ByteArray(16)
        System.arraycopy(mcfgBinary,15,connKey,0,16)
        packet.connKey = connKey

        //网络密钥   Bytes:16   index:31~46    MSB~LSB
        var netKey = ByteArray(16)
        System.arraycopy(mcfgBinary,31,netKey,0,16)
        packet.netKey = netKey

        //64位目标地址  MSB~LSB  Bytes:8  index:47~54
        var destAddr = ByteArray(8)
        System.arraycopy(mcfgBinary,47,destAddr,0,8)
        packet.destAddr = destAddr

        // 保留       Bytes:20     index:55~74
        var rsv2 = ByteArray(20)
        System.arraycopy(mcfgBinary,55,rsv2,0,20)
        packet.rsv2 = rsv2

        return packet
    }

}