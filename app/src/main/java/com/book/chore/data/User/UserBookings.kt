package com.book.chore.data.User

// data model for booking
class UserBookings(
    bmcdataId: String,
    name: String,
    date: String,
    serviceName: String,
    profileURL: String,
    Duration: String,
    TotalCost: String
) {
    var id: String = bmcdataId
    var Name: String= name
    var Date: String = date
//    var hourlyRate: String = ""
    var ServiceName: String = serviceName
//    var RatingValue: Double = 0.0
    var profileurl: String = profileURL
    var duration: String = Duration
    var totalCost: String = TotalCost
}
