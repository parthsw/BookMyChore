package com.book.chore.ui.services

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.book.chore.R
import com.book.chore.ui.login.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_booking_confirmed.view.*


class booking_confirmed : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var serviceType = arguments?.getString("taskType")
        var taskerName = arguments?.getString("tasker")
        var bookingID = arguments?.getString("uid")
        var datee = arguments?.getString("sDate")
        var taskTime = arguments?.getString("sTime")
        var duration = arguments?.getString("duration")
        var totalAmount = arguments?.getString("totalCost")

        val view: View = inflater!!.inflate(R.layout.fragment_booking_confirmed, container, false)

        view.bID.setText(bookingID)
        view.date.setText(datee)
        view.tTime.setText(taskTime)
        view.tDuration.setText(duration)
        view.tType.setText(serviceType)
        view.tName.setText(taskerName)
        view.tAmount.setText(totalAmount)

        view.homeButton.setOnClickListener{
            val i = Intent(requireContext(), HomeActivity::class.java)
            startActivity(i)
            findNavController(this).popBackStack()
        }

        view.setBackgroundColor(Color.WHITE);
        // Inflate the layout for this fragment
        return view
    }
}

