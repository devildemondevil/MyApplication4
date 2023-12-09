package com.aspsine.fragmentnavigator.demo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aspsine.fragmentnavigator.demo.Action

/**
 * Created by aspsine on 16/9/3.
 */
object BroadcastManager {
    fun register(context: Context?, receiver: BroadcastReceiver?, vararg actions: String?) {
        val filter = IntentFilter()
        for (action in actions) {
            filter.addAction(action)
        }
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver!!, filter)
    }

    fun unregister(context: Context?, receiver: BroadcastReceiver?) {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver!!)
    }

    fun sendLoginBroadcast(context: Context?, position: Int) {
        val intent = Intent(Action.LOGIN)
        intent.putExtra("EXTRA_POSITION", position)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }

    fun sendLogoutBroadcast(context: Context?, position: Int) {
        val intent = Intent(Action.LOGOUT)
        intent.putExtra("EXTRA_POSITION", position)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }
}