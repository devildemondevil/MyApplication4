package com.aspsine.fragmentnavigator.demo.utils

import android.content.Context

/**
 * Created by aspsine on 16/9/2.
 */
object SharedPrefUtils {
    private const val SHARED_PREF_LOGIN = "login"
    fun login(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_LOGIN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("login", true)
        editor.commit()
    }

    fun logout(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_LOGIN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("login", false)
        editor.commit()
    }

    fun isLogin(context: Context?): Boolean {
        val sharedPreferences =
            context!!.getSharedPreferences(SHARED_PREF_LOGIN, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("login", false)
    }
}