package com.book.chore.data.Doer

// data model for ChoreDoer
class ChoreDoer(){
    var user_ID: String = ""
    var userDisplayName: String = ""
    var userMobile: String = ""
    var userProfilePic: String = ""
    var userDescription: String = ""
    var rating: String = ""
    var hourlyRate: String = ""
    var service_type = arrayListOf<String>()
    var userLongDesc: String = ""
    lateinit var userComments: Map<String, List<Comment>>
    var city : String = ""
}