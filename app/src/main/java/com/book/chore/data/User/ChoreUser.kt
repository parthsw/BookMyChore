package com.book.chore.data.User

// data model for ChoreUser
data class ChoreUser(
    var userID: String = "",
    var userDisplayName: String = "",
    var userEmail: String = "",
    var userMobile: String = "",
    var userAddress: String = "",
    var userProfilePic: String = "",
    var userBirthDate: String = "",
    var userPassword: String = ""
)