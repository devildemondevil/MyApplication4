package com.aspsine.fragmentnavigator.demo.eventbus

import android.os.Bundle

class MessageActionEvent(var action:ActionType) {
    var bundle = Bundle()


    fun put(value: String): MessageActionEvent {
        bundle.putString("KEY_STRING", value)
        return this
    }

    fun getString(): String? {
        return bundle.getString("KEY_STRING")
    }


    enum class ActionType{
        DevConnected,
        DevDisConnected
    }
}