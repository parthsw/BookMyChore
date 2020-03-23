package com.book.chore.ui.login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.book.chore.R
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.LoginFormBinding
import com.book.chore.ui.login.home.HomeActivity
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants

class LoginViewHolder {
    fun bindData(binding: LoginFormBinding, context: Context, finishActivity: () -> Unit) {
        with(binding) {
            register.setOnClickListener {
                context.startActivity(Intent(context, SignUpActivity::class.java))
            }
            login.setOnClickListener {
                UserManager().attemptSignIn(email.text.toString(), password.text.toString()) {
                    if (it.success) {
                        BasePrefs.putValue(
                            ChoreConstants.PrefNames.PREF_NAME_USER,
                            ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN,
                            true
                        )
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        finishActivity()
                        Toast.makeText(context, "Successfully Logged in", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.loginFailed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}