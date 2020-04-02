package com.book.chore.ui.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.UserManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.book.chore.R
import com.book.chore.ui.login.LoginActivity
import com.book.chore.ui.login.home.HomeActivity
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage


class SplashActivity : AppIntro()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlowAnimation()
        if(com.book.chore.data.User.UserManager().isUserLoggedIn())
        {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        else {
            val sliderPage = SliderPage()
            val sliderPage1 = SliderPage()
            val sliderPage2 = SliderPage()
            val sliderPage3 = SliderPage()

            sliderPage.title = "Quality service"
            sliderPage.description = "Get your home chores done through our team of trusted taskman at your doorstep"
            sliderPage.imageDrawable = R.drawable.truseted
            sliderPage.bgColor = R.color.about_background_color

            sliderPage1.title = "Trusted Professional"
            sliderPage1.description = "We provide only verified and experienced professional"
            sliderPage1.imageDrawable = R.drawable.verified
            sliderPage1.bgColor = R.color.about_background_color

            sliderPage2.title = "Matched to Your needs"
            sliderPage2.description = "We match with the right professional according to your budget and time"
            sliderPage2.imageDrawable = R.drawable.match
            sliderPage2.bgColor = R.color.about_background_color

            sliderPage3.title = "Flexible Timing"
            sliderPage3.description = "You have the freedom to choose the day and time when you want the work to be completed"
            sliderPage3.imageDrawable = R.drawable.flexibletiming
            sliderPage3.bgColor = R.color.about_background_color


            addSlide(AppIntroFragment.newInstance(sliderPage))
            addSlide(AppIntroFragment.newInstance(sliderPage1))
            addSlide(AppIntroFragment.newInstance(sliderPage2))
            addSlide(AppIntroFragment.newInstance(sliderPage3))



            setBarColor(Color.parseColor("#2c397f"));
            setSeparatorColor(Color.parseColor("#1A253F"));


            showSkipButton(true);
            isProgressButtonEnabled = true
            setProgressButtonEnabled(true)

        }
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        Toast.makeText(applicationContext,"Welcome",Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        // Do something when users tap on Skip button.
    }
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Toast.makeText(applicationContext,"Welcome",Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        //Toast.makeText(applicationContext,"Welcome",Toast.LENGTH_SHORT).show()

    }
    override fun onSlideChanged(
        @Nullable oldFragment: Fragment?,
        @Nullable newFragment: Fragment?
    ) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }
}
