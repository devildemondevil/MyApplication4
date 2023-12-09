package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import android.renderscript.Byte2

/**
 * page:105
 * Explicit Addressing ZigBee Command Frame
 */
class ApiZigbeeCFPacket {
    /**
     * Frame Type       Bytes:1  index:0
     */
    var frameType :Byte = 0x11

    /**
     * Frame Id        Bytes:1  index:1
     */

    var frameId :Byte = 0

    /**
     * 64-bit Destination Address  MSB~LSB  Bytes:8  index:2~9
     */
    var destAddr :ByteArray = ByteArray(8)

    /**
     * 16-bit Destination Network Address MSB~LSB  Bytes:2  index:10~11
     */
    var destNetAddr :Int = 0

    /**
     * Source Endpoint  Bytes:1    index:12
     */
    var srcEndpoint :Byte = 0

    /**
     * Destination Endpoint Bytes:1  index:13
     */

    var destEndpoint :Byte = 0

    /**
     * Cluster ID Bytes: 2  index:14~15
     */

    var clusterId :Int = 0

    /**
     * Profile ID Bytes:2  index:16~17
     */

    var profileId :Int = 0

    /**
     * Broadcast Radius  Bytes:1  index:18
     */
    var broadcastRadius :Byte = 0


    /**
     * Transmit Options Bytes:1  index:19
     */
    var tranOpt :Byte = 0


    /**
     * Data Payload  Byte:?  index:19~End
     */
    lateinit var dataPayload : ByteArray
}