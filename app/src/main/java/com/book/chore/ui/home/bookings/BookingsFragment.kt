package com.book.chore.ui.home.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.book.chore.R
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.BookingsFragmentBinding
import com.book.chore.ui.login.LoginViewHolder

class BookingsFragment : Fragment() {

    private lateinit var binding: BookingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookings, container, false)
        if (UserManager().isUserLoggedIn()) {
            binding.loginForm.container.visibility = View.GONE
            binding.bookingsLayout.visibility = View.VISIBLE
        } else {
            binding.loginForm.container.visibility = View.VISIBLE
            context?.let {
                LoginViewHolder().bindData(binding.loginForm, it) {
                    activity?.finish()
                }
            }
            binding.bookingsLayout.visibility = View.GONE
        }
        return binding.root
    }
}