package com.book.chore.ui.services

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.Time.MONDAY_BEFORE_JULIAN_EPOCH
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.book.chore.R
import com.book.chore.data.User.UserBookings
import com.book.chore.data.User.UserManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_booking_confirmation.*
import kotlinx.android.synthetic.main.fragment_booking_confirmation.view.*
import java.text.SimpleDateFormat
import java.util.*


var format = SimpleDateFormat("dd MMM YYYY" , Locale.CANADA)
//for timeformat
var timeformat= SimpleDateFormat("hh:mm a", Locale.CANADA)
var hours : Long = 1
var taskerRate = 0;
var serviceName = ""
var taskerName = ""
var date = ""
var profileURL = ""
var total: Long = 0
var ttR = ""
var starttime = ""
var endtime = ""
var bmcdataId = ""


class booking_confirmation : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_booking_confirmation, container, false)
        view.confirm_booking.isEnabled = false


        view.dButton.setOnClickListener{
            //getting date
            val now = Calendar.getInstance()
            val cyear: Int = now.get(Calendar.YEAR)
            val cmonth: Int = now.get(Calendar.MONTH)
            val cday: Int = now.get(Calendar.DAY_OF_MONTH)
            val datepicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selecteddate = Calendar.getInstance()
                    selecteddate.set(Calendar.YEAR, year)
                    selecteddate.set(Calendar.MONTH, month)
                    selecteddate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = format.format(selecteddate.time)
                    tvdate.setText(date)
                    //Toast.makeText(requireContext(), "year" + year, Toast.LENGTH_SHORT).show()
                },
                cyear,
                cmonth,
                cday
            )
            datepicker.getDatePicker().setMinDate(now.getTimeInMillis());
            now.add(Calendar.DAY_OF_MONTH, 7);
            datepicker.getDatePicker().setMaxDate(now.getTimeInMillis());
            datepicker.show()
        }

        view.time.setOnClickListener {
            val now = Calendar.getInstance()
            val timepicker = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectortime = Calendar.getInstance()
                    selectortime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectortime.set(Calendar.MINUTE, minute)
                    starttime = timeformat.format(selectortime.time)
                    tvtime.setText(timeformat.format(selectortime.time))
                    Toast.makeText(
                        requireContext(),
                        "time" + timeformat.format(selectortime.time),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            timepicker.show()

        }

        view.btntaskcomplete.setOnClickListener {
            val now = Calendar.getInstance()
            val timepicker = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectortime = Calendar.getInstance()
                    selectortime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectortime.set(Calendar.MINUTE, minute)
                    endtime = timeformat.format(selectortime.time)
                    tvapproximatetime.setText(timeformat.format(selectortime.time))
                    Toast.makeText(
                        requireContext(),
                        "time" + timeformat.format(selectortime.time),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            timepicker.show()

        }


        // to see the hours..it will be helpful to calculate the price
        view.tvapproximatetime.doAfterTextChanged {

            if (starttime.isEmpty()) {
                Toast.makeText(requireContext(),"Please Select Start Time",Toast.LENGTH_SHORT).show()

            } else {
                var date1 = timeformat.parse(starttime);
                var date2 = timeformat.parse(endtime);
                val difference = date2.time - date1.time
                var days = (difference / (1000 * 60 * 60 * 24)).toInt()
                hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)) as Long
                var min =
                    (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) as Long / (1000 * 60)
                hours = if (hours < 0) -hours else hours
                if(min >= 30)
                    hours += 1
                tvtotal.setText(hours.toString() + " Hour(s)")
                total = hours * taskerRate
                ttc.setText("$" + total.toString())
                confirm_booking.isEnabled = true
            }
        }

        view.tvtime.doAfterTextChanged {
            if (endtime.isEmpty() || endtime == "") {
                Toast.makeText(requireContext(),"Please Select End Time",Toast.LENGTH_SHORT).show()
            } else {
                var date1 = timeformat.parse(starttime);
                var date2 = timeformat.parse(endtime);
                val difference = date2.time - date1.time
                var days = (difference / (1000 * 60 * 60 * 24)).toInt()
                hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)) as Long
                var min =
                    (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) as Long / (1000 * 60)
                hours = if (hours < 0) -hours else hours
                if(min >= 30)
                    hours += 1
                tvtotal.setText(hours.toString() + " Hour(s)")
                total = hours * taskerRate
                ttc.setText("$" + total.toString())
                confirm_booking.isClickable = true
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.intent?.let(this::populateFromIntent)
        confirm_booking.setOnClickListener {
            book()
        }
    }

    private fun populateFromIntent(intent: Intent) {

        profileURL = intent.getStringExtra("profileURL")

        serviceName = intent.getStringExtra("taskType")
        task.text = serviceName

        taskerName = intent.getStringExtra("tasker")
        tasker.text = taskerName

        ttR = intent.getStringExtra("taskerRate")
        thr.text = ttR

        taskerRate = ttR.substring(1,3).toInt()
    }

    private fun book(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("BOOKING CONFIRMATION")

        builder.setMessage("Confirm Booking?")
        builder.setPositiveButton("YES"){_, _ ->
            sendToFirebase()
            setInBundle()
        }
        builder.setNegativeButton("No"){_, _->
            Toast.makeText(requireContext(),"Please check details again.",Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("Cancel"){_, _->
            Toast.makeText(requireContext(),"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun setInBundle(){
        val bundle = Bundle()
        bundle.putString("taskType", serviceName)
        bundle.putString("tasker", taskerName)
        bundle.putString("uid", bmcdataId)
        bundle.putString("sDate", date)
        bundle.putString("sTime", starttime)
        bundle.putString("duration", hours.toString())
        bundle.putString("totalCost", "$"+total.toString())
        findNavController().navigate(R.id.action_booking_confirmation_to_booking_confirmed, bundle)
    }

    private fun sendToFirebase(){
        //Insert into Firebase
        val uID = MONDAY_BEFORE_JULIAN_EPOCH
        val ref = FirebaseDatabase.getInstance().getReference(UserManager().loggedInUserId())
        bmcdataId = ref.push().key ?: uID.toString()
        val taskposter = UserBookings(bmcdataId, taskerName, date, serviceName, profileURL, hours.toString(), "$" + total.toString())
        ref.child(bmcdataId).setValue(taskposter).addOnCompleteListener{
            Toast.makeText(requireContext(), "completed", Toast.LENGTH_LONG).show()
        }
    }
}
