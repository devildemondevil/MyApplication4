package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import android.renderscript.Byte2

/**
 * page:112
 * ZigBee Explicit Rx Indicator
 */
class ApiZigbeeERIPacket {
    /**
     * Frame Type       Bytes:1  index:0
     */
    var frameType :Byte = 0x91.toByte()

    /**
     * 64-bit Source Address  MSB~LSB  Bytes:8  index:1~8
     */
    var srcAddr :ByteArray = ByteArray(8)

    /**
     * 16-bit Source Network Address MSB~LSB  Bytes:2  index:9~10
     */
    var srcNetAddr :Int = 0

    /**
     * Source Endpoint  Bytes:1    index:11
     */
    var srcEndpoint :Byte = 0

    /**
     * Destination Endpoint Bytes:1  index:12
     */

    var destEndpoint :Byte = 0

    /**
     * Cluster ID Bytes: 2  index:13~14
     */

    var clusterId :Int = 0

    /**
     * Profile ID Bytes:2  index:15~16
     */

    var profileId :Int = 0

    /**
     * Receive Options  Bytes:1  index:17
     */
    var recvOpt :Byte = 0

    /**
     * Received Data   Byte:?  index:18~
     */
    lateinit var recvData : ByteArray
}