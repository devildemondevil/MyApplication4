package com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData

import android.os.Parcel
import android.os.Parcelable
import android.renderscript.Byte2
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCtPacket

/**
 * page:103
 * frameType
 * AT Command : 0x08
 * AT Command - Queue Parameter Value  :0x09
 */
class ApiZigbeeATCmdAndQpPacket() : Parcelable {
    /**
     * Frame Type       Bytes:1  index:0
     */
    var frameType :Byte = 0x08

    /**
     * Frame Id        Bytes:1  index:1
     */

    var frameId :Byte = 0x00

    /**
     * AT Command     Bytes:2   index:2~3
     */

    var atCmd :String = "00"

    /**
     * Parameter Value  Byte:0~256  optional  index:4~End
     */
    lateinit var  parasValues : ByteArray

    fun isParasValueInitialzed()=::parasValues.isInitialized

    constructor(parcel: Parcel) : this() {
        frameType = parcel.readByte()
        frameId = parcel.readByte()
        atCmd = parcel.readString().toString()
        parasValues = parcel.createByteArray()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(frameType)
        parcel.writeByte(frameId)
        parcel.writeString(atCmd)
        parcel.writeByteArray(parasValues)
    }

    companion object CREATOR : Parcelable.Creator<ApiZigbeeATCmdAndQpPacket> {
        override fun createFromParcel(parcel: Parcel): ApiZigbeeATCmdAndQpPacket {
            return ApiZigbeeATCmdAndQpPacket(parcel)
        }

        override fun newArray(size: Int): Array<ApiZigbeeATCmdAndQpPacket?> {
            return arrayOfNulls(size)
        }
    }


}