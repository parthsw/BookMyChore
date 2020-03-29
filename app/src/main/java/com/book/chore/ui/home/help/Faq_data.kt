package com.book.chore.ui.home.help

class Faq_data {


    var header:MutableList<String> =ArrayList()
    var body:MutableList<MutableList<String>> =ArrayList()
    var payment_header:MutableList<String> =ArrayList()
    var payment_body:MutableList<MutableList<String>> =ArrayList()
    var ans1:MutableList<String> =ArrayList()
    var ans2:MutableList<String> =ArrayList()
    var ans3:MutableList<String> =ArrayList()
    var ans4:MutableList<String> =ArrayList()
    var ans5:MutableList<String> =ArrayList()
    var ans6:MutableList<String> =ArrayList()
    var ans7:MutableList<String> =ArrayList()
    var ans8:MutableList<String> =ArrayList()
    var ans9:MutableList<String> =ArrayList()

    fun question_related_to_booking():MutableList<String>
    {
        header.add("How to book a service?")
        header.add("How to cancel a booking?")
        header.add("How can i contact the Tasker?")
        header.add("can i reschedule my booking?")
        header.add("How to know if my request is confirmed?")
        header.add("What if no service provider is available in my locality?")
        return header
    }

    fun answer_of_booking(){
        ans1.add("You can book the service by \n" +
                "1) Click the servic you want from the home page\n" +
                "2) Select service provider according to your need.\n" +
                "3) Select the day and time")
        ans2.add("if you want to cancel a booking due to some reason \n" +
                "you can do that by going to my booking page\n" +
                "select the booking and cancel or reschedule the booking")
        ans3.add("It is very easy to contact with your booked tasker.\n" +
                "You just nedd to go to your booking tab and " +
                "you will find all the contact detail of your booked tasker")
        ans4.add("Yes! you can reschedule your booking anytime by going into my booking tab and select the booking you want to reschedule")
        ans5.add("You can check you booking status by going into the mybooking tab.\n" +
                "On that tab you will be able to see your " +
                "booking along with booking status")
        ans6.add("Sorry to hear that.You can try to request the service by contacting us.")

    }
    fun answer_of_payment(){
        ans7.add("You can pay for the service either before or after service delivery.\n" +
                "You can pay using Debit/Credit card while booking the service")
        ans8.add("If you cancel your booking ,you refund process will initiate automatically")
        ans9.add("Sorry.Currently we are not accepting any other mode of payment except Debit card/Credit card and cash")
    }




    fun answer_related_to_booking():MutableList<MutableList<String>>
    {
        answer_of_booking()
        body.add(ans1)
        body.add(ans2)
        body.add(ans3)
        body.add(ans4)
        body.add(ans5)
        body.add(ans6)
        return body
    }


    fun question_related_to_payment():MutableList<String>
    {

        payment_header.add("How to pay for the service")
        payment_header.add("Refund Policy")
        payment_header.add("Can i pay using other E-wallet?")
        return   payment_header
    }

    fun answer_related_to_payment():MutableList<MutableList<String>>
    {

        answer_of_payment()
        payment_body.add(ans7)
        payment_body.add(ans8)
        payment_body.add(ans9)
        return payment_body
    }
}