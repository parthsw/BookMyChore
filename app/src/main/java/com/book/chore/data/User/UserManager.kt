package com.book.chore.data.User

import android.graphics.drawable.Drawable
import com.book.chore.R
import com.book.chore.databinding.ProfileBinding
import com.book.chore.events.UserCreationEvent
import com.book.chore.events.UserSignInResultEvent
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.profile_form.view.*
import org.greenrobot.eventbus.EventBus


class UserManager {
    fun showLoginScreen(): Boolean {
        return BasePrefs.getBoolean(
            ChoreConstants.PrefNames.PREF_NAME_USER,
            ChoreConstants.PrefKeys.PREF_KEY_SKIPPED_LOGIN
        ) || isUserLoggedIn()
    }

    fun isUserLoggedIn(): Boolean {
        return BasePrefs.getBoolean(
            ChoreConstants.PrefNames.PREF_NAME_USER,
            ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN
        )
    }

    fun loggedInUserId(): String {
        return BasePrefs.getString(
            ChoreConstants.PrefNames.PREF_NAME_USER,
            ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN_USER_ID
        ).toString()
    }

    fun attemptSignIn(
        email: String,
        password: String,
        userSignInResultEvent: (UserSignInResultEvent) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_USERS)
            .whereEqualTo(ChoreConstants.Parameters.userEmail, email)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    if (!it.isEmpty) {
                        for (doc in it) {
                            if ((doc.toObject(ChoreUser::class.java)).userPassword == password) {
                                userSignInResultEvent(UserSignInResultEvent(true))
                                BasePrefs.putValue(
                                    ChoreConstants.PrefNames.PREF_NAME_USER,
                                    ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN_USER_ID,
                                    (doc.toObject(ChoreUser::class.java)).userID
                                )
                                break
                            } else {
                                userSignInResultEvent(UserSignInResultEvent(false))
                            }
                        }
                    } else {
                        userSignInResultEvent(UserSignInResultEvent(false))
                    }
                }
            }
    }

    fun createUser(
        userDisplayName: String,
        userEmail: String,
        userMobile: String = "",
        userAddress: String,
        userProfilePic: String,
        userPassword: String
    ) {
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_USERS).add(
            ChoreUser(
                "",
                userDisplayName,
                userEmail,
                userMobile,
                userAddress,
                userProfilePic,
                "",
                userPassword
            )
        ).addOnSuccessListener {
            updateUserId(it.id)
        }.addOnFailureListener {

        }
    }

    private fun updateUserId(userId: String) {
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_USERS)
            .document(userId)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    val choreUser = document.toObject(ChoreUser::class.java)
                    choreUser?.userID = userId
                    BasePrefs.putValue(
                        ChoreConstants.PrefNames.PREF_NAME_USER,
                        ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN,
                        true
                    )
                    BasePrefs.putValue(
                        ChoreConstants.PrefNames.PREF_NAME_USER,
                        ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN_USER_ID,
                        userId
                    )
                    choreUser?.let {
                        FirebaseFirestore.getInstance()
                            .collection(ChoreConstants.Collections.CHORE_USERS)
                            .document(userId).set(it).addOnSuccessListener {
                                EventBus.getDefault().post(UserCreationEvent())
                            }
                    }
                }
            }.addOnFailureListener { exception ->
            }
    }

    fun renderUserData(userId: String, binding: ProfileBinding, userData: (ChoreUser?) -> Unit) {
        fetchUserDataById(userId) {

            val resImg: Drawable = binding.profileForm.resources.getDrawable(R.drawable.upload_picture)

            if (it == null) {
                userData(null)
                binding.profileForm.img.setImageDrawable(resImg)
            } else {
                with(it) {
                    binding.profileForm.edtUserName.setText(userID)
                    binding.profileForm.edtUserEmail.setText(userEmail)
                    binding.profileForm.edtUserAddress.setText(userAddress)
                    binding.profileForm.edtUserMobile.setText(userMobile)
                    binding.profileForm.edtUserPassword.setText(userPassword)
                    if (userProfilePic == "null" || userProfilePic == "") {
                        binding.profileForm.img.setImageDrawable(resImg)
                    } else {
                        Glide.with(binding.profileForm).load(userProfilePic).into(binding.profileForm.img)
                    }
                    userData(this)
                }
            }
        }
    }

    fun fetchUserDataById(userId: String, userData: (ChoreUser?) -> Unit) {
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_USERS)
            .document(userId)
            .get().addOnSuccessListener { document ->
                userData(document.toObject(ChoreUser::class.java))
            }.addOnFailureListener {
                userData(null)
            }
    }

    fun updateUserProfile(user: ChoreUser, updateResult: () -> Unit) {
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_USERS)
            .document(user.userID).set(user).addOnCompleteListener {
                updateResult()
            }
    }

    fun logout() {
        BasePrefs.removeKey(
            ChoreConstants.PrefNames.PREF_NAME_USER,
            ChoreConstants.PrefKeys.PREF_KEY_SKIPPED_LOGIN
        )
        BasePrefs.removeKey(
            ChoreConstants.PrefNames.PREF_NAME_USER,
            ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN
        )
        BasePrefs.removeKey(
            ChoreConstants.PrefNames.PREF_NAME_USER,
            ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN_USER_ID
        )
    }
}