package com.book.chore.ui.home.profile

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.book.chore.R
import com.book.chore.data.User.ChoreUser
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.ProfileBinding
import com.book.chore.ui.login.LoginActivity
import com.book.chore.ui.login.LoginViewHolder
import com.book.chore.utils.ChoreValidators
import kotlinx.android.synthetic.main.profile_form.*
import kotlinx.android.synthetic.main.profile_form.view.*
import kotlinx.android.synthetic.main.profile_form.view.edtUserAddress
import kotlinx.android.synthetic.main.profile_form.view.edtUserEmail
import kotlinx.android.synthetic.main.profile_form.view.edtUserMobile
import kotlinx.android.synthetic.main.profile_form.view.edtUserName
import kotlinx.android.synthetic.main.profile_form.view.edtUserPassword
import java.lang.Exception

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileBinding
    private var user: ChoreUser? = null
    var obj=Validatedata()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
            renderViews()
        } catch (e: Exception) {
            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun renderViews() {
                binding.updateProgress.visibility = View.GONE
        if (UserManager().isUserLoggedIn()) {
            binding.loginForm.container.visibility = View.GONE
            binding.profileForm.visibility = View.VISIBLE
            binding.btnUpdateProfile.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.VISIBLE

            UserManager().renderUserData(UserManager().loggedInUserId(), binding) {
                user = it
            }
            binding.btnUpdateProfile.setOnClickListener {

                if (obj.validatepassword(edtUserPassword.text.toString())!="perfect")
                {
                    edtUserPassword.setError(obj.validatepassword(edtUserPassword.text.toString()))
                }
                else if (obj.validateemail(edtUserEmail.text.toString())!="perfect")
                {
                    edtUserEmail.setError(obj.validateemail(edtUserEmail.text.toString()))
                }
                else if (obj.validatemobileno(edtUserMobile.text.toString())!="perfect")
                {
                    edtUserMobile.setError(obj.validatemobileno(edtUserMobile.text.toString()))
                }
                else if(obj.validateusername(edtUserName.text.toString())!="perfect")
                {
                    edtUserName.setError(obj.validateusername(edtUserName.text.toString()))
                }
                else if(obj.validateaddress(edtUserAddress.text.toString())!="perfect")
                {
                    edtUserAddress.setError(obj.validateaddress(edtUserAddress.text.toString()))
                }
                else {
                    user?.let { it1 ->
                        binding.updateProgress.visibility = View.VISIBLE
                        binding.btnUpdateProfile.visibility = View.GONE
                        UserManager().updateUserProfile(it1) {
                            binding.updateProgress.visibility = View.GONE
                            binding.btnUpdateProfile.visibility = View.VISIBLE
                        }
                    }
                }
                binding.profileForm.edtUserName.addTextChangedListener { username ->
                    user?.userDisplayName = username.toString()
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
            }


            binding.btnLogout.setOnClickListener {


                val sharedPreferences: SharedPreferences?= activity?.getSharedPreferences("user_prefs",
                    Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.clear()
                editor?.apply()
                Toast.makeText(activity,"Successfully logout from the application",Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)

            }

        } else {
            binding.loginForm.container.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
            context?.let {
                LoginViewHolder().bindData(binding.loginForm, it) {
                    activity?.finish()
                }
            }
            binding.profileForm.visibility = View.GONE
            binding.btnUpdateProfile.visibility = View.GONE

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

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    // handling presentational state of login button
    private fun enableLoginButton(isEnable: Boolean) {
        binding.loginForm.login.alpha = if (isEnable) 1f else 0.7f
        binding.loginForm.login.isEnabled = isEnable
    }
}