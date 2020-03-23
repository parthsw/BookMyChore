package com.book.chore.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.book.chore.R
import com.book.chore.databinding.LoginBinding
import com.book.chore.ui.login.home.HomeActivity
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants.PrefKeys.Companion.PREF_KEY_SKIPPED_LOGIN
import com.book.chore.utils.ChoreConstants.PrefNames.Companion.PREF_NAME_USER

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        LoginViewHolder().bindData(loginBinding.loginForm, this@LoginActivity) {
            finishActivity()
        }
        loginBinding.btnSkipLogin.setOnClickListener {
            BasePrefs.putValue(PREF_NAME_USER, PREF_KEY_SKIPPED_LOGIN, true)
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finishActivity()
        }
    }

    private fun finishActivity() {
        finish()
    }
}