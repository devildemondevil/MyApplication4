package com.aspsine.fragmentnavigator.demo.eventbus

import android.os.Bundle
import android.os.Parcelable
import com.aspsine.fragmentnavigator.demo.protocols.MCFGCtHeaderCmdType
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.McfgCtPacket

private const val KEY_INT = "key_int"
private const val KEY_FLOAT = "key_float"
private const val KEY_STRING = "key_string"
private const val KEY_BYTES = "key_bytes"
private const val KEY_BOOL = "key_bool"
private const val KEY_PARCELABLE = "key_parcelable"
private const val KEY_MCFGCTPACKET = "key_mcfgct_packet"

data class McgfCtEvent(var rwType: RwType,var mcfgCtHeaderCmdType: MCFGCtHeaderCmdType){
    var bundle = Bundle()

    fun put(value:Int):McgfCtEvent{
        bundle.putInt(KEY_INT,value)
        return this
    }

    fun put(value:Float):McgfCtEvent{
        bundle.putFloat(KEY_FLOAT,value)
        return this
    }

    fun put(value: String): McgfCtEvent {
        bundle.putString(KEY_STRING, value)
        return this
    }

    fun put(value: ByteArray): McgfCtEvent {
        bundle.putByteArray(KEY_BYTES, value)
        return this
    }

    fun put(value: Boolean): McgfCtEvent {
        bundle.putBoolean(KEY_BOOL, value)
        return this
    }

    fun put(value: Parcelable): McgfCtEvent {
        bundle.putParcelable(KEY_PARCELABLE, value)
        return this
    }

    fun put(value : McfgCtPacket) :McgfCtEvent{
        bundle.putParcelable(KEY_MCFGCTPACKET, value)
        return this
    }

    //-------------

    fun put(key: String, value: Int): McgfCtEvent {
        bundle.putInt(key, value)
        return this
    }

    fun put(key: String,value:Float):McgfCtEvent{
        bundle.putFloat(key,value)
        return this
    }


    fun put(key: String, value: String): McgfCtEvent {
        bundle.putString(key, value)
        return this
    }

    fun put(key: String,value: ByteArray): McgfCtEvent {
        bundle.putByteArray(key, value)
        return this
    }

    fun put(key: String, value: Boolean): McgfCtEvent {
        bundle.putBoolean(key, value)
        return this
    }


    fun put(key: String, value: Parcelable): McgfCtEvent {
        bundle.putParcelable(key, value)
        return this
    }

    fun put(key: String, value : McfgCtPacket) :McgfCtEvent{
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

    fun <T : Parcelable> getMcfgCtPacket(): T? {
        return bundle.getParcelable<T>(KEY_MCFGCTPACKET)
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

    fun <T : Parcelable> getMcfgCtPacket(key: String): T? {
        return bundle.getParcelable<T>(key)
    }

    enum class RwType {
        MsgSend,
        MsgRecv,
    }
}
