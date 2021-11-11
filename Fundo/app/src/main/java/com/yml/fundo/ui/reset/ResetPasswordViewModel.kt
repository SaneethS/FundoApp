package com.yml.fundo.ui.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.auth.Authentication

class ResetPasswordViewModel: ViewModel() {
    private val _resetPasswordStatus = MutableLiveData<String>()
    val resetPasswordStatus = _resetPasswordStatus as LiveData<String>

    fun resetPasswordOfUser(email: String){
        Authentication.resetPassword(email){
            _resetPasswordStatus.value = it
        }
    }
}