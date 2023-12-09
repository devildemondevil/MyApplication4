package com.aspsine.fragmentnavigator.demo.protocols.zigbee

import android.os.Parcel
import android.os.Parcelable
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCtPacket

/**
 * page :99
 * 9. API Operation
 */
class ZigBeePacket {
    /**
     * 帧头 开始    1字节
     */
    var startDelimiter :Byte= 0x7E

    /**
     * 长度   2~3   MSB-LSB
     */
    var length :Int= 0

    /**
     * 数据
     */
    lateinit var frameData: ByteArray

    /**
     * 校验
     */
    var checkSum :Byte = 0


    /**
     * Api Id
     */
    var apiId :Byte = 0

    /**
     * Api Data
     */
    lateinit var apiData :ByteArray

}