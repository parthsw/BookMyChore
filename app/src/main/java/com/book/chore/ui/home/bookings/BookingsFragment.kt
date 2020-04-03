package com.book.chore.ui.home.bookings

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.chore.R
import com.book.chore.data.User.UserBookings
import com.book.chore.data.User.UserHistory
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.BookingsFragmentBinding
import com.book.chore.ui.home.bookings.adapter.UserBookingsAdapter
import com.book.chore.ui.login.LoginViewHolder
import com.book.chore.utils.ChoreValidators
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class BookingsFragment : Fragment() {

    private lateinit var binding: BookingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        try {
            binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_bookings, container, false)
            if (UserManager().isUserLoggedIn()) {
                binding.loginForm.container.visibility = View.GONE
                binding.bookingsLayout.visibility = View.VISIBLE
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                retrievedata()
            } else {
                binding.loginForm.container.visibility = View.VISIBLE
                context?.let {
                    LoginViewHolder().bindData(binding.loginForm, it) {
                        activity?.finish()
                    }
                }
                binding.bookingsLayout.visibility = View.GONE

                enableLoginButton(false)
                var emailField = binding.loginForm.email
                var passwordField = binding.loginForm.password
                var isEmailValid = false

                emailField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        isEmailValid = ChoreValidators.isEmailValid(s.toString())
                        if (!isEmailValid) {
                            ChoreValidators.setError(
                                emailField,
                                resources.getString(R.string.invalidEmail)
                            )
                            context?.let {
                                ChoreValidators.setInputFieldColor(
                                    emailField, android.R.color.holo_red_dark,
                                    it
                                )
                            }
                        } else {
                            ChoreValidators.clearError(emailField)
                            context?.let {
                                ChoreValidators.setInputFieldColor(
                                    emailField, R.color.colorPrimary,
                                    it
                                )
                            }
                        }
                        if (s.toString().isNotEmpty() && passwordField.text.toString().isNotEmpty() && isEmailValid) {
                            enableLoginButton(true)
                        } else {
                            enableLoginButton(false)
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                })

                passwordField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString().isNotEmpty() && emailField.text.toString().isNotEmpty() && isEmailValid) {
                            enableLoginButton(true)
                        } else {
                            enableLoginButton(false)
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                })
            }
        } catch (e: Exception) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // handling presentational state of login button
    private fun enableLoginButton(isEnable: Boolean) {

        binding.loginForm.login.alpha = if (isEnable) 1f else 0.7f
        binding.loginForm.login.isEnabled = isEnable
    }

    private fun retrievedata() {
        val userID = UserManager().loggedInUserId()
        Log.i(ContentValues.TAG, userID)
        val ref = FirebaseDatabase.getInstance().getReference(userID)
        val bmclist = mutableListOf<UserHistory>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error getting data", Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (pika in p0.children) {
                        val pikadata = pika.getValue(UserHistory::class.java)
                        bmclist.add(pikadata!!)
                        Log.i(ContentValues.TAG, "$pikadata")
                    }
                    binding.recyclerView.adapter= UserBookingsAdapter(this@BookingsFragment , bmclist)
                }
                else {
                    binding.bookingempty.visibility = View.VISIBLE
                }
            }

        })

    }
}