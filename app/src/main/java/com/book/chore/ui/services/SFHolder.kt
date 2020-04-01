package com.book.chore.ui.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.book.chore.R

// Intent arguments for Activity (in "extras" bundle) -- string values.
const val USER_DETAILS_ID_KEY = "id"
const val USER_DETAILS_FIRST_NAME_KEY = "firstName"
const val USER_DETAILS_EMAIL_KEY = "email"
const val USER_DETAILS_PROFILE_PHOTO_URL_KEY = "profilePhotoUrl"

class SFHolder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.services_fragment_holder_activity)

        if (savedInstanceState != null) {
            val fragment = ConfirmBookingFragment()
            fragment.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.my_nav_host_fragment,fragment)
                .commit()
        }
    }

}
