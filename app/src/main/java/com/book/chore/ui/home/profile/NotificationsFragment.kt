package com.book.chore.ui.home.profile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.book.chore.R
import com.book.chore.data.User.ChoreUser
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.ProfileBinding
import com.book.chore.ui.login.LoginActivity
import com.book.chore.ui.login.LoginViewHolder
import com.book.chore.utils.ChoreConstants
import com.book.chore.utils.ChoreValidators
import com.book.chore.utils.ImageManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.profile_form.*
import kotlinx.android.synthetic.main.profile_form.view.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileBinding
    private var picturePath: Uri? = null
    private var user: ChoreUser? = null

    var obj = Validatedata()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
            binding.profileForm.img.setOnClickListener {
                context?.let { it1 -> selectOrCaptureImage(it1) }
            }
            renderViews()
        } catch (e: Exception) {
            Toast.makeText(context, resources.getString(R.string.profileFormError), Toast.LENGTH_SHORT).show()
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

            binding.btnUpdateProfile.setOnClickListener {
                if(picturePath != null) {
                    uploadImage()
                } else {
                    updateUserProfile(picturePath.toString())
                }
            }

            binding.btnLogout.setOnClickListener {
                val sharedPreferences: SharedPreferences?= activity?.getSharedPreferences("user_prefs",
                    Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.clear()
                editor?.apply()
                UserManager().logout()
                Toast.makeText(activity,resources.getString(R.string.logout_success),Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }

        } else {
            binding.loginForm.container.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
            binding.profileForm.img.setImageDrawable(resources.getDrawable(R.drawable.upload_picture))
            context?.let {
                LoginViewHolder().bindData(binding.loginForm, it) {
                    activity?.finish()
                }
            }
            binding.profileForm.visibility = View.GONE
            binding.btnUpdateProfile.visibility = View.GONE

            validateLoginForm()
        }
    }

    // handling presentational state of login button
    private fun enableLoginButton(isEnable: Boolean) {
        binding.loginForm.login.alpha = if (isEnable) 1f else 0.7f
        binding.loginForm.login.isEnabled = isEnable
    }

    private fun selectOrCaptureImage(context: Context) {
        try {
            var imgSelectionOptions: Array<CharSequence> = arrayOf(
                resources.getString(R.string.imgTakePhoto),
                resources.getString(R.string.imgChooseFromGallery),
                resources.getString(R.string.imgCancel)
            )
            var imgSelectionModal = AlertDialog.Builder(context)

            imgSelectionModal.setTitle(resources.getString(R.string.imgSelectionTitle))
            imgSelectionModal.setItems(
                imgSelectionOptions
            ) { dialogInterface: DialogInterface, i: Int ->
                when {
                    imgSelectionOptions[i] == resources.getString(R.string.imgTakePhoto) -> {

                        var imgFile: File? = null
                        try {
                            imgFile = ImageManager().createImageFile(context)
                        } catch (e: IOException) {
                            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
                        }

                        val intent = Intent()
                        intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                        var photoUri: Uri? = null
                        if (imgFile != null) {
                            photoUri = FileProvider.getUriForFile(context, "com.book.chore.provider", imgFile)
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        }
                        picturePath = photoUri

                        startActivityForResult(
                            Intent.createChooser(
                                intent,
                                resources.getString(R.string.imgTakePhoto)
                            ), ChoreConstants.Images.CAPTURE_IMAGE
                        );

                    }
                    imgSelectionOptions[i] == resources.getString(R.string.imgChooseFromGallery) -> {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(
                                intent,
                                resources.getString(R.string.imgChooseFromGallery)
                            ),
                            ChoreConstants.Images.CHOOSE_FROM_GALLERY
                        )
                    }
                    else -> {
                        dialogInterface.dismiss()
                    }
                }
            }
            imgSelectionModal.show();
        } catch (e: Exception) {
            Toast.makeText(activity, resources.getString(R.string.imgModalError), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChoreConstants.Images.CHOOSE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            try {
                picturePath = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, picturePath)
                img.setImageBitmap(bitmap)
            } catch (e: IOException) {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.imgFromGalleryError),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (requestCode == ChoreConstants.Images.CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Glide.with(this).load(picturePath).into(img)
            }
            catch (e: Exception) {
                Toast.makeText(activity, resources.getString(R.string.imgCapturedError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage() {
        if (picturePath != null) {
            val ref = FirebaseStorage.getInstance()
                .reference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(picturePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val profilePicture = downloadUri.toString()
                        updateUserProfile(profilePicture)
                    } else { }
                }?.addOnFailureListener {}
        } else { }
    }

    private fun updateUserProfile(img: String) {

        if (obj.validatepassword(edtUserPassword.text.toString())!="perfect")
        {
            edtUserPassword.error = obj.validatepassword(edtUserPassword.text.toString())
        }
        else if (obj.validateemail(edtUserEmail.text.toString())!="perfect")
        {
            edtUserEmail.error = obj.validateemail(edtUserEmail.text.toString())
        }
        else if (obj.validatemobileno(edtUserMobile.text.toString())!="perfect")
        {
            edtUserMobile.error = obj.validatemobileno(edtUserMobile.text.toString())
        }
        else if(obj.validateusername(edtUserName.text.toString())!="perfect")
        {
            edtUserName.error = obj.validateusername(edtUserName.text.toString())
        }
        else if(obj.validateaddress(edtUserAddress.text.toString())!="perfect")
        {
            edtUserAddress.error = obj.validateaddress(edtUserAddress.text.toString())
        }
        else {
            if(user?.userProfilePic == null || user?.userProfilePic == "") {
                user?.userProfilePic = if (img == "null") { "" } else { img }
            }
            user?.let { it1 ->
                binding.updateProgress.visibility = View.VISIBLE
                binding.btnUpdateProfile.isEnabled = false
                binding.btnUpdateProfile.alpha = 0.7f
                UserManager().updateUserProfile(it1) {
                    binding.updateProgress.visibility = View.GONE
                    binding.btnUpdateProfile.visibility = View.VISIBLE
                    binding.btnUpdateProfile.isEnabled = true
                    binding.btnUpdateProfile.alpha = 1f
                    Toast.makeText(activity, resources.getString(R.string.updateSuccess), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateLoginForm() {
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