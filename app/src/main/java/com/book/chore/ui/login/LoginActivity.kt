package com.book.chore.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.book.chore.R
import com.book.chore.databinding.LoginBinding
import com.book.chore.ui.login.home.HomeActivity
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants.PrefKeys.Companion.PREF_KEY_SKIPPED_LOGIN
import com.book.chore.utils.ChoreConstants.PrefNames.Companion.PREF_NAME_USER
import com.book.chore.utils.ChoreValidators
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
            LoginViewHolder().bindData(loginBinding.loginForm, this@LoginActivity) {
                finishActivity()
            }

            enableLoginButton(false)
            var emailField = loginBinding.loginForm.email
            var passwordField = loginBinding.loginForm.password
            var isEmailValid = false

            emailField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    isEmailValid = ChoreValidators.isEmailValid(s.toString())
                    if (!isEmailValid) {
                        ChoreValidators.setError(
                            emailField,
                            resources.getString(R.string.invalidEmail)
                        )
                        ChoreValidators.setInputFieldColor(
                            emailField,
                            android.R.color.holo_red_dark,
                            this@LoginActivity
                        )
                    } else {
                        ChoreValidators.clearError(emailField)
                        ChoreValidators.setInputFieldColor(
                            emailField,
                            R.color.colorPrimary,
                            this@LoginActivity
                        )
                    }

                    if (s.toString().isNotEmpty() && passwordField.text.toString().isNotEmpty() && isEmailValid) {
                        enableLoginButton(true)
                    } else {
                        enableLoginButton(false)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })

            passwordField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty() && emailField.text.toString().isNotEmpty() && isEmailValid) {
                        enableLoginButton(true)
                    } else {
                        enableLoginButton(false)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            loginBinding.btnSkipLogin.setOnClickListener {
                BasePrefs.putValue(PREF_NAME_USER, PREF_KEY_SKIPPED_LOGIN, true)
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finishActivity()
            }
        } catch (e: Exception) {
            Toast.makeText(this@LoginActivity, "", Toast.LENGTH_SHORT).show()
        }

    }

    private fun finishActivity() {
        finish()
    }

    // handling presentational state of login button
    private fun enableLoginButton(isEnable: Boolean) {
        loginBinding.loginForm.login.alpha = if (isEnable) 1f else 0.7f
        loginBinding.loginForm.login.isEnabled = isEnable
    }
}