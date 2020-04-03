package com.book.chore.ui.services

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.book.chore.R

class SFHolder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.services_fragment_holder_activity)

        if (savedInstanceState != null) {
            val fragment = booking_confirmation()
            fragment.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.my_nav_host_fragment,fragment)
                .commit()
        }
    }

}
