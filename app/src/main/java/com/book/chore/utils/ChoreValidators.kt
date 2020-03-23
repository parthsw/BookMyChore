package com.book.chore.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.widget.EditText
import androidx.core.content.ContextCompat

class ChoreValidators {
    companion object {
        // https://gist.github.com/ironic-name/f8e8479c76e80d470cacd91001e7b45b
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPhoneValid(phone: String): Boolean {
            return phone.length == 10
        }

        fun setError(inputField: EditText, error: String) {
            inputField.error = error
        }

        fun clearError(inputField: EditText) {
            inputField.error = null
        }

        // Updating the input field color
        @SuppressLint("NewApi")
        fun setInputFieldColor(editText: EditText, color: Int, context: Context) {
            editText.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, color))
        }
    }
}