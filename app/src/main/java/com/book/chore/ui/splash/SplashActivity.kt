package com.book.chore.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.book.chore.R
import com.book.chore.data.User.UserManager
import com.book.chore.ui.login.LoginActivity
import com.book.chore.ui.login.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(
                this@SplashActivity,
                if (UserManager().showLoginScreen()) HomeActivity::class.java else LoginActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 3000)
    }
}