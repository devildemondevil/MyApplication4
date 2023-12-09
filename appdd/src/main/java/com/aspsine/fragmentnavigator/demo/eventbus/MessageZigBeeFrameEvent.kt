package com.aspsine.fragmentnavigator.demo.eventbus

import android.os.Bundle
import android.os.Parcelable
import com.aspsine.fragmentnavigator.demo.protocols.ApiFrameType
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdAndQpPacket
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdRespPacket

private const val KEY_INT = "key_int"
private const val KEY_FLOAT = "key_float"
private const val KEY_STRING = "key_string"
private const val KEY_BYTES = "key_bytes"
private const val KEY_BOOL = "key_bool"
private const val KEY_PARCELABLE = "key_parcelable"
private const val KEY_API_AT_CMD_AND_QP_PACKET = "key_api_at_cmd_and_qp_packet"
private const val KEY_APT_AT_CMD_RESP_PACKET = "key_api_at_cmd_resp_packet"

data class MessageZigBeeFrameEvent(var apiFrameType: ApiFrameType){
    var bundle = Bundle()

    fun put(value:Int):MessageZigBeeFrameEvent{
        bundle.putInt(KEY_INT,value)
        return this
    }

    fun put(value:Float):MessageZigBeeFrameEvent{
        bundle.putFloat(KEY_FLOAT,value)
        return this
    }

    fun put(value: String): MessageZigBeeFrameEvent {
        bundle.putString(KEY_STRING, value)
        return this
    }

    fun put(value: ByteArray): MessageZigBeeFrameEvent {
        bundle.putByteArray(KEY_BYTES, value)
        return this
    }

    fun put(value: Boolean): MessageZigBeeFrameEvent {
        bundle.putBoolean(KEY_BOOL, value)
        return this
    }

    fun put(value: Parcelable): MessageZigBeeFrameEvent {
        bundle.putParcelable(KEY_PARCELABLE, value)
        return this
    }

    fun put(value : ApiZigbeeATCmdAndQpPacket) :MessageZigBeeFrameEvent{
        bundle.putParcelable(KEY_API_AT_CMD_AND_QP_PACKET, value)
        return this
    }

    fun put(value : ApiZigbeeATCmdRespPacket) :MessageZigBeeFrameEvent{
        bundle.putParcelable(KEY_APT_AT_CMD_RESP_PACKET, value)
        return this
    }


    //-------------

    fun put(key: String, value: Int): MessageZigBeeFrameEvent {
        bundle.putInt(key, value)
        return this
    }

    fun put(key: String,value:Float):MessageZigBeeFrameEvent{
        bundle.putFloat(key,value)
        return this
    }


    fun put(key: String, value: String): MessageZigBeeFrameEvent {
        bundle.putString(key, value)
        return this
    }

    fun put(key: String,value: ByteArray): MessageZigBeeFrameEvent {
        bundle.putByteArray(key, value)
        return this
    }

    fun put(key: String, value: Boolean): MessageZigBeeFrameEvent {
        bundle.putBoolean(key, value)
        return this
    }


    fun put(key: String, value: Parcelable): MessageZigBeeFrameEvent {
        bundle.putParcelable(key, value)
        return this
    }

    fun put(key: String, value : ApiZigbeeATCmdAndQpPacket) :MessageZigBeeFrameEvent{
        bundle.putParcelable(key, value)
        return this
    }

    fun put(key: String, value : ApiZigbeeATCmdRespPacket) :MessageZigBeeFrameEvent{
        bundle.putParcelable(key, value)
        return this
    }


    //===============================================================

    fun getInt(): Int {
        return bundle.getInt(KEY_INT)
    }

    fun getFloat(): Float {
        return bundle.getFloat(KEY_FLOAT)
    }

    fun getString(): String? {
        return bundle.getString(KEY_STRING)
    }

    fun getBytes(): ByteArray? {
        return bundle.getByteArray(KEY_BYTES)
    }

    fun getBoolean(): Boolean {
        return bundle.getBoolean(KEY_BOOL)
    }

    fun <T : Parcelable> getParcelable(): T? {
        return bundle.getParcelable<T>(KEY_PARCELABLE)
    }

    fun <T : Parcelable> getApiAtCmdAndQpPacket(): T? {
        return bundle.getParcelable<T>(KEY_API_AT_CMD_AND_QP_PACKET)
    }

    fun <T : Parcelable> getApiAtCmdRespPacket(): T? {
        return bundle.getParcelable<T>(KEY_APT_AT_CMD_RESP_PACKET)
    }

    //--------------------

    fun getInt(key: String): Int {
        return bundle.getInt(key)
    }

    fun getFloat(key: String): Float {
        return bundle.getFloat(key)
    }

    fun getString(key: String): String? {
        return bundle.getString(key)
    }

    fun getBytes(key: String): ByteArray? {
        return bundle.getByteArray(key)
    }

    fun getBoolean(key: String): Boolean {
        return bundle.getBoolean(key)
    }

    fun <T : Parcelable> getParcelable(key: String): T? {
        return bundle.getParcelable<T>(key)
    }

    fun <T : Parcelable> getApiAtCmdAndQpPacket(key: String): T? {
        return bundle.getParcelable<T>(key)
    }

    fun <T : Parcelable> getApiAtCmdRespPacket(key: String): T? {
        return bundle.getParcelable<T>(key)
    }
}
