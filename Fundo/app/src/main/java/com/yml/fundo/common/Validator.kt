package com.yml.fundo.common

import android.content.Context
import android.util.Log
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.yml.fundo.R

object Validator {

    fun registrationValidation(name:TextInputEditText,
                               email: TextInputEditText,
                               password: TextInputEditText,
                               confirmPassword: TextInputEditText
                               ,mobileNo: TextInputEditText,
    context:Context): Boolean
    {
        var status = true
        if(validateName(name.text.toString())){
            name.setError(context.getString(R.string.validate_name))
            status = false
        }
        if(validateEmail(email.text.toString())){
            email.setError(context.getString(R.string.validate_email))
            status = false
        }
        if(validateMobileNo(mobileNo.text.toString())){
            mobileNo.setError(context.getString(R.string.validate_mobile_no))
            status = false
        }
        if(validatePassword( password.text.toString())){
            password.setError(context.getString(R.string.validate_password))
            confirmPassword.setError(context.getString(R.string.validate_password))
            status = false
        }
        if(password.text.toString() != confirmPassword.text.toString()){
            confirmPassword.setError(context.getString(R.string.password_confirm_password))
            status = false
        }
        Log.i("validator","status = $status")
        return status
    }

    fun loginValidation(email: TextInputEditText,
                        password: TextInputEditText,
    context: Context): Boolean{
        var status = true
        if(validateEmail(email.text.toString())){
            email.setError(context.getString(R.string.validate_email))
            status = false
        }
        if(validatePassword( password.text.toString())){
            password.setError(context.getString(R.string.validate_password))
            status = false
        }
        return status
    }

    fun forgotPasswordValidator(email: TextInputEditText, context: Context):Boolean{
        var status = true
        if(validateEmail(email.text.toString())){
            email.setError(context.getString(R.string.validate_email))
            status = false
        }
        return status
    }

    fun validateName(name: String) = !name.isNotEmpty()
    fun validateEmail(email: String) = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun validateMobileNo(mobileNo: String) = !Patterns.PHONE.matcher(mobileNo).matches()
    fun validatePassword(password: String) = !(password.length > 6)
}