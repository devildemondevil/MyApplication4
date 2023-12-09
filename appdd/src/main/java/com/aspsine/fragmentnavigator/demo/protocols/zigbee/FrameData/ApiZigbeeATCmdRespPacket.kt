package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import android.os.Parcel
import android.os.Parcelable

/**
 * AT Command Response
 * page:109
 * frameType :0x88
 */
class ApiZigbeeATCmdRespPacket() : Parcelable {
    /**
     * Frame Type       Bytes:1  index:0
     */
    var frameType :Byte = (0x88).toByte()

    /**
     * Frame Id        Bytes:1  index:1
     */

    var frameId :Byte = 0x00

    /**
     * AT Command     Bytes:2   index:2~3
     */

    var atCmd :String = "00"

    /**
     * Command Status   Bytes:1  index:4
     */
    var cmdStatus :Byte = 0x00

    /**
     * Parameter Value  Byte:0~256  optional  index:5~End
     */
    lateinit var  cmdData : ByteArray

    fun isParasValueInitialzed()=::cmdData.isInitialized

    constructor(parcel: Parcel) : this() {
        frameType = parcel.readByte()
        frameId = parcel.readByte()
        atCmd = parcel.readString().toString()
        cmdStatus = parcel.readByte()
        cmdData = parcel.createByteArray()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(frameType)
        parcel.writeByte(frameId)
        parcel.writeString(atCmd)
        parcel.writeByte(cmdStatus)
        parcel.writeByteArray(cmdData)
    }

    companion object CREATOR : Parcelable.Creator<ApiZigbeeATCmdRespPacket> {
        override fun createFromParcel(parcel: Parcel): ApiZigbeeATCmdRespPacket {
            return ApiZigbeeATCmdRespPacket(parcel)
        }

        override fun newArray(size: Int): Array<ApiZigbeeATCmdRespPacket?> {
            return arrayOfNulls(size)
        }
    }


}