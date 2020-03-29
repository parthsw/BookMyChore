package com.book.chore.ui.home.help

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.provider.Settings.System.getString
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.TypedArrayUtils.getString
import com.book.chore.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class About_us : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)


        var yourString: String =getString(R.string.about_us_info)
        var obj=Element()


        val aboutPage = AboutPage(this)
            .isRTL(false)
            .setImage(R.drawable.logo)
            . setDescription(yourString)
            .addItem(obj.setTitle("Version 1.0"))
            .addGroup("Connect with us")
            .addEmail("parth.parmar@dal.ca")
            .addWebsite("https://www.google.com/")
            .addFacebook("")
            .addPlayStore("")
            .addInstagram("Book_my_chore")
            .create()


        setContentView(aboutPage)

    }


}
