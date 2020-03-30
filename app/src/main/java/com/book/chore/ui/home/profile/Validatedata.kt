package com.book.chore.ui.home.profile

import android.telephony.PhoneNumberUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

class Validatedata {
    fun validatepassword(str:String):String
    {
            if(str.isEmpty())
            {
                return ("Please enter password")
            }
            else if (str.contains(" "))
            {
                return ("Password should not contain nay space")
            }
            else{
                str?.let {
                    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
                    val passwordMatcher = Regex(passwordPattern)
                    //if (passwordMatcher.find(str) != null)
                    return ("perfect")
                } ?:  return (" your password must contain 1 uppercase,1 lower case and 1 digit")
            }
                    return (" your password must contain 1 uppercase,1 lower case and 1 digit")
    }
    fun validateemail(str:String):String
    {
            if(str.isEmpty())
            {
                return ("Please enter email id")
            }
            else if (str.contains(" "))
            {
                return ("Email id should not contain any space")
            }
            else
            {
                    if(Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                        return ("perfect")
                    }
                    else
                        return ("Enter valid email")
            }

    }
    fun validatemobileno(str: String):String
    {
            if(str.isEmpty())
            {
                return ("Enter mobile number")
            }
            else if(str.length!=10)
            {
                return ("Please enter valid number")
            }
            else
            {
                  if (PhoneNumberUtils.isGlobalPhoneNumber(str))
                  {
                      return ("perfect")
                  }
                    else
                      return ("enter valid number")
            }

    }

    fun validateusername(str:String):String
    {
        if(str.isEmpty())
            return ("Enter username")
        else if(str.length<5)
            return ("Username must be more than 5 character")
        else
            return ("perfect")
    }

    fun validateaddress(str:String):String
    {
        if(str.isEmpty())
            return "Enter address"
        else
            return "perfect"
    }


}