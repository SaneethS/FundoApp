package com.yml.fundo.util

import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

object Validator {

    fun registrationValidation(name:TextInputEditText,
                               email: TextInputEditText,
                               password: TextInputEditText,
                               confirmPassword: TextInputEditText
                               ,mobileNo: TextInputEditText): Boolean
    {
        var status:Boolean = true
        if(validateName(name.text.toString())){
            name.setError("Enter valid name")
            status = false
        }
        if(validateEmail(email.text.toString())){
            email.setError("Enter valid Email")
            status = false
        }
        if(validateMobileNo(mobileNo.text.toString())){
            mobileNo.setError("Enter valid mobile number")
            status = false
        }
        if(validatePassword( password.text.toString())){
            password.setError("Enter valid password")
            confirmPassword.setError("Enter valid password")
            status = false
        }
        if(password.text.toString() != confirmPassword.text.toString()){
            confirmPassword.setError("Enter same Password")
            status = false
        }
        Log.i("validator","status = $status")
        return status
    }

    fun loginValidation(email: TextInputEditText,
                        password: TextInputEditText): Boolean{
        var status:Boolean = true
        if(validateEmail(email.text.toString())){
            email.setError("Enter valid Email")
            status = false
        }
        if(validatePassword( password.text.toString())){
            password.setError("Enter valid password")
            status = false
        }
        return status
    }

    fun validateName(name: String) = !name.isNotEmpty()
    fun validateEmail(email: String) = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun validateMobileNo(mobileNo: String) = !Patterns.PHONE.matcher(mobileNo).matches()
    fun validatePassword(password: String) = !(password.length > 6)
}