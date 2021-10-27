package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.service.Authentication

class ResetPasswordViewModel: ViewModel() {
    private val _resetPasswordStatus = MutableLiveData<String>()
    val resetPasswordStatus = _resetPasswordStatus as LiveData<String>

    fun resetPasswordOfUser(email: String){
        Authentication.resetPassword(email){
            _resetPasswordStatus.value = it
        }
    }
}