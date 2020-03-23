package com.book.chore.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.book.chore.R
import com.book.chore.data.User.ChoreUser
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.ProfileBinding
import com.book.chore.ui.login.LoginViewHolder
import kotlinx.android.synthetic.main.profile_form.view.*

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileBinding
    private var user: ChoreUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        renderViews()
        return binding.root
    }

    private fun renderViews() {
        if (UserManager().isUserLoggedIn()) {
            binding.loginForm.container.visibility = View.GONE
            binding.profileForm.visibility = View.VISIBLE
            binding.btnUpdateProfile.visibility = View.VISIBLE
            UserManager().renderUserData(UserManager().loggedInUserId(), binding) {
                user = it
            }
            binding.btnUpdateProfile.setOnClickListener {
                user?.let { it1 ->
                    binding.updateProgress.visibility = View.VISIBLE
                    binding.btnUpdateProfile.visibility = View.GONE
                    UserManager().updateUserProfile(it1) {
                        binding.updateProgress.visibility = View.GONE
                        binding.btnUpdateProfile.visibility = View.VISIBLE
                    }
                }
            }
            binding.profileForm.edtUserEmail.addTextChangedListener { userEmail ->
                user?.userEmail = userEmail.toString()
            }
            binding.profileForm.edtUserMobile.addTextChangedListener { userMobile ->
                user?.userMobile = userMobile.toString()
            }
            binding.profileForm.edtUserPassword.addTextChangedListener { userPassword ->
                user?.userPassword = userPassword.toString()
            }
            binding.profileForm.edtUserAddress.addTextChangedListener { userAddress ->
                user?.userAddress = userAddress.toString()
            }

        } else {
            binding.loginForm.container.visibility = View.VISIBLE
            context?.let {
                LoginViewHolder().bindData(binding.loginForm, it) {
                    activity?.finish()
                }
            }
            binding.profileForm.visibility = View.GONE
            binding.btnUpdateProfile.visibility = View.GONE
        }
    }
}