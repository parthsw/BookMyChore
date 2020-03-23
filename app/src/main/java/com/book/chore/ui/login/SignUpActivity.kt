package com.book.chore.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.book.chore.R
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.SignUpBinding
import com.book.chore.events.UserCreationEvent
import com.book.chore.events.UserCreationFailedEvent
import kotlinx.android.synthetic.main.profile_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: SignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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
}