package com.book.chore.ui.services

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.book.chore.R
import com.book.chore.data.ChoreServices.ChoreService
import com.book.chore.data.ChoreServices.ServiceManager
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.HomeFragmentBinding
import com.book.chore.ui.home.home.adapters.ChoreServicesAdapter
import com.book.chore.ui.home.home.adapters.OnItemClickListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.service_frag_conf_page.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class ConfirmBookingFragment : Fragment() {

    private lateinit var binding: ConfirmBookingFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val textView = textView5
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.text = sdf.format(cal.time)

        }

        textView.setOnClickListener {
            DatePickerDialog(
                this.context!!, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

//        return binding.root
        return inflater.inflate(R.layout.service_frag_conf_page, container, false)
    }

    private fun populateFromIntent(intent: Intent) {
        //nameTxt.text = intent.getStringExtra(USER_DETAILS_FIRST_NAME_KEY)
        //Glide.with(this).load(intent.getStringExtra(USER_DETAILS_PROFILE_PHOTO_URL_KEY)).into(imageView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.intent?.let(this::populateFromIntent)
        // new navigation style example:
        textView5.setOnClickListener {
            //Added Action ID to traverse from 1st fragment to 2nd
        }
    }
}
