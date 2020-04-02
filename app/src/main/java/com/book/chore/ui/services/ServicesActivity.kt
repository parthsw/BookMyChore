package com.book.chore.ui.services

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.chore.R
import com.book.chore.data.Doer.TDManager
import com.book.chore.utils.ChoreConstants
import kotlinx.android.synthetic.main.activity_services.*


class ServicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val city = intent.getStringExtra(ChoreConstants.AppConstant.SERVICE_CITY)
        TDManager().retrieveData (this@ServicesActivity, recyclerView, city)

//        val fragment = ConfirmBookingFragment()
//        fragment.arguments = intent.extras
//        supportFragmentManager.beginTransaction()
//            .add(R.id.task_doers_details,fragment)
//            .commit()

    }

    private fun finishActivity() {
        finish()
    }



}