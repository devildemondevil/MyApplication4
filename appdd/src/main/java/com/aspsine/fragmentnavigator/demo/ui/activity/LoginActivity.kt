package com.aspsine.fragmentnavigator.demo.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.aspsine.fragmentnavigator.demo.R
import com.aspsine.fragmentnavigator.demo.broadcast.BroadcastManager
import com.aspsine.fragmentnavigator.demo.utils.SharedPrefUtils

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        val button = findViewById<View>(R.id.login_in_button) as Button
        button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.login_in_button) {
            tryLogin()
        }
    }

    fun tryLogin() {
        val email = etEmail!!.text.toString().trim { it <= ' ' }
        val password = etPassword!!.text.toString().trim { it <= ' ' }
        if (check(email, password)) {
            markUserLogin()
            notifyUserLogin()
            finish()
        }
    }

    fun check(email: String?, password: String?): Boolean {
        if (TextUtils.isEmpty(email)) {
            etEmail!!.error = getString(R.string.error_invalid_email)
            return false
        }
        if (TextUtils.isEmpty(password)) {
            etPassword!!.error = getString(R.string.error_invalid_password)
            return false
        }
        return true
    }

    private fun markUserLogin() {
        SharedPrefUtils.login(this)
    }

    private fun notifyUserLogin() {
        BroadcastManager.sendLoginBroadcast(this, 1)
    }
}