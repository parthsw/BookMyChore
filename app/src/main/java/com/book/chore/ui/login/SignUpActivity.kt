package com.book.chore.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.book.chore.R
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.SignUpBinding
import com.book.chore.events.UserCreationEvent
import com.book.chore.events.UserCreationFailedEvent
import com.book.chore.ui.login.home.HomeActivity
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants
import com.book.chore.utils.ChoreValidators
import kotlinx.android.synthetic.main.profile_form.*
import kotlinx.android.synthetic.main.profile_form.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: SignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

            signUpBinding.signUp.setOnClickListener {
                signUpBinding.loading.visibility = View.VISIBLE
                UserManager().createUser(
                    edtUserName.text.toString(),
                    edtUserEmail.text.toString(),
                    edtUserMobile.text.toString(),
                    edtUserAddress.text.toString(),
                    "",
                    edtUserPassword.text.toString()
                )
                Toast.makeText(this@SignUpActivity, "Registration Successful", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
            }

            signUpBinding.profileForm.imgProfile.setOnClickListener {
            }

            enableRegisterButton(false)
            var emailField = signUpBinding.profileForm.edtUserEmail
            var passwordField = signUpBinding.profileForm.edtUserPassword
            var phoneField = signUpBinding.profileForm.edtUserMobile
            var addressField = signUpBinding.profileForm.edtUserAddress

            var isEmailValid = false
            var isPhoneValid = false

            // Email + Phone + Address + Password
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
                            this@SignUpActivity
                        )
                    } else {
                        ChoreValidators.clearError(emailField)
                        ChoreValidators.setInputFieldColor(
                            emailField,
                            R.color.colorPrimary,
                            this@SignUpActivity
                        )
                    }

                    if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && addressField.text.isNotEmpty() && passwordField.text.isNotEmpty()) {
                        enableRegisterButton(true)
                    } else {
                        enableRegisterButton(false)
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
                    if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && addressField.text.isNotEmpty() && emailField.text.isNotEmpty()) {
                        enableRegisterButton(true)
                    } else {
                        enableRegisterButton(false)
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

            phoneField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    isPhoneValid = ChoreValidators.isPhoneValid(s.toString())
                    if (!isPhoneValid) {
                        ChoreValidators.setError(
                            phoneField,
                            resources.getString(R.string.invalidPhone)
                        )
                        ChoreValidators.setInputFieldColor(
                            phoneField,
                            android.R.color.holo_red_dark,
                            this@SignUpActivity
                        )
                    } else {
                        ChoreValidators.clearError(phoneField)
                        ChoreValidators.setInputFieldColor(
                            phoneField,
                            R.color.colorPrimary,
                            this@SignUpActivity
                        )
                    }
                    if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && addressField.text.isNotEmpty() && passwordField.text.isNotEmpty()) {
                        enableRegisterButton(true)
                    } else {
                        enableRegisterButton(false)
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

            addressField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && passwordField.text.isNotEmpty()) {
                        enableRegisterButton(true)
                    } else {
                        enableRegisterButton(false)
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
        } catch (e: Exception) {
            Toast.makeText(
                this.applicationContext,
                resources.getString(R.string.registerFailed),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserCreationEvent) {
        signUpBinding.loading.visibility = View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserCreationFailedEvent) {
        Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show()
        signUpBinding.loading.visibility = View.GONE
    }

    // handling presentational state of login button
    private fun enableRegisterButton(isEnable: Boolean) {
        signUpBinding.signUp.alpha = if (isEnable) 1f else 0.7f
        signUpBinding.signUp.isEnabled = isEnable
    }
}
