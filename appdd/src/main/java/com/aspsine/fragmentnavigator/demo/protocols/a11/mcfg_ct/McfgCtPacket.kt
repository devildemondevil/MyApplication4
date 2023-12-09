package com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct

import android.os.Parcel
import android.os.Parcelable
import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeader

/**
 * 表 C.1 数据帧头定义 + 其他数据
 */
class McfgCtPacket() : Parcelable {

    /**
     * 消息头  Bytes:10  index:0~9
     */
    var a11packetHeader : A11PacketHeader = A11PacketHeader()

    /**
     * 命令类型 Bytes:1  index:10
     */
    var cmdType : Byte = 0

    /**
     * 后续数据  Bytes:剩余   index:11~End
     */
    lateinit var FollowUpData: ByteArray



    constructor(parcel: Parcel) : this() {
        cmdType = parcel.readByte()
        FollowUpData = parcel.createByteArray()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(cmdType)
        parcel.writeByteArray(FollowUpData)
    }

    companion object CREATOR : Parcelable.Creator<McfgCtPacket> {
        override fun createFromParcel(parcel: Parcel): McfgCtPacket {
            return McfgCtPacket(parcel)
        }

        override fun newArray(size: Int): Array<McfgCtPacket?> {
            return arrayOfNulls(size)
        }
    }

}