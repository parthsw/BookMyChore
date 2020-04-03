package com.book.chore.ui.login

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.book.chore.R
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.SignUpBinding
import com.book.chore.events.UserCreationEvent
import com.book.chore.events.UserCreationFailedEvent
import com.book.chore.ui.login.home.HomeActivity
import com.book.chore.utils.ChoreConstants
import com.book.chore.utils.ChoreValidators
import com.book.chore.utils.ImageManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.profile_form.*
import kotlinx.android.synthetic.main.profile_form.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.IOException
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: SignUpBinding

    private var picturePath: Uri? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
            storageReference = FirebaseStorage.getInstance().reference

            validateProfileForm()

            signUpBinding.profileForm.img.setOnClickListener { selectOrCaptureImage(this) }
            signUpBinding.signUp.setOnClickListener {

                signUpBinding.loading.visibility = View.VISIBLE
                signUpBinding.signUp.visibility = View.GONE
                if(picturePath != null) {
                    uploadImage()
                } else {
                    createUser("")
                }

            }
        } catch (e: Exception) {
            Toast.makeText(
                this.applicationContext,
                resources.getString(R.string.registerFormError),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserCreationEvent) {
        signUpBinding.loading.visibility = View.GONE
        Toast.makeText(
            this@SignUpActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        )
            .show()
        startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserCreationFailedEvent) {
        Toast.makeText(this, resources.getString(R.string.registerFailed), Toast.LENGTH_LONG).show()
        signUpBinding.loading.visibility = View.GONE
        signUpBinding.loading.visibility = View.VISIBLE
    }

    // handling presentational state of login button
    private fun enableRegisterButton(isEnable: Boolean) {
        signUpBinding.signUp.alpha = if (isEnable) 1f else 0.7f
        signUpBinding.signUp.isEnabled = isEnable
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
                            imgFile = ImageManager().createImageFile(this)
                        } catch (e: IOException) {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                        val intent = Intent()
                        intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                        var photoUri: Uri? = null
                        if (imgFile != null) {
                            photoUri = FileProvider.getUriForFile(this, "com.book.chore.provider", imgFile)
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
            Toast.makeText(this, resources.getString(R.string.imgModalError), Toast.LENGTH_SHORT)
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
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, picturePath)
                img.setImageBitmap(bitmap)
            } catch (e: IOException) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.imgFromGalleryError),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (requestCode == ChoreConstants.Images.CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Glide.with(this).load(picturePath).into(img)
            }
            catch (e: Exception) {
                Toast.makeText(this, resources.getString(R.string.imgCapturedError), Toast.LENGTH_SHORT).show()
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
                        createUser(profilePicture)
                    } else { }
                }?.addOnFailureListener {}
        } else { }
    }

    // delegating user creation to UserManager service
    private fun createUser(profilePicture: String) {
        UserManager().createUser(
            edtUserName.text.toString(),
            edtUserEmail.text.toString(),
            edtUserMobile.text.toString(),
            edtUserAddress.text.toString(),
            profilePicture,
            edtUserPassword.text.toString()
        )
    }

    // run time profile form field validations while registering a new user
    private fun validateProfileForm() {
        enableRegisterButton(false)
        var emailField = signUpBinding.profileForm.edtUserEmail
        var passwordField = signUpBinding.profileForm.edtUserPassword
        var phoneField = signUpBinding.profileForm.edtUserMobile
        var addressField = signUpBinding.profileForm.edtUserAddress

        var isEmailValid = false
        var isPhoneValid = false

        // Email + Phone + Address + Password
        emailField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isEmailValid = ChoreValidators.isEmailValid(s.toString())
                if (!isEmailValid) {
                    ChoreValidators.setError(
                        emailField,
                        resources.getString(R.string.invalidEmail)
                    )
                    ChoreValidators.setInputFieldColor(
                        emailField,
                        android.R.color.holo_red_dark,
                        this@SignUpActivity
                    )
                } else {
                    ChoreValidators.clearError(emailField)
                    ChoreValidators.setInputFieldColor(
                        emailField,
                        R.color.colorPrimary,
                        this@SignUpActivity
                    )
                }

                if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && addressField.text!!.isNotEmpty() && passwordField.text!!.isNotEmpty()) {
                    enableRegisterButton(true)
                } else {
                    enableRegisterButton(false)
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
                if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && addressField.text!!.isNotEmpty() && emailField.text!!.isNotEmpty()) {
                    enableRegisterButton(true)
                } else {
                    enableRegisterButton(false)
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

        phoneField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isPhoneValid = ChoreValidators.isPhoneValid(s.toString())
                if (!isPhoneValid) {
                    ChoreValidators.setError(
                        phoneField,
                        resources.getString(R.string.invalidPhone)
                    )
                    ChoreValidators.setInputFieldColor(
                        phoneField,
                        android.R.color.holo_red_dark,
                        this@SignUpActivity
                    )
                } else {
                    ChoreValidators.clearError(phoneField)
                    ChoreValidators.setInputFieldColor(
                        phoneField,
                        R.color.colorPrimary,
                        this@SignUpActivity
                    )
                }
                if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && addressField.text!!.isNotEmpty() && passwordField.text!!.isNotEmpty()) {
                    enableRegisterButton(true)
                } else {
                    enableRegisterButton(false)
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

        addressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && isEmailValid && isPhoneValid && passwordField.text!!.isNotEmpty()) {
                    enableRegisterButton(true)
                } else {
                    enableRegisterButton(false)
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
