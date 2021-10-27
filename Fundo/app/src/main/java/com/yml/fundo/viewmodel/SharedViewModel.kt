package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.yml.fundo.fragments.ResetPassword
import com.yml.fundo.model.User
import com.yml.fundo.service.Authentication

class SharedViewModel: ViewModel() {
    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    val goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToRegisterPageStatus = MutableLiveData<Boolean>()
    val goToRegisterPageStatus = _goToRegisterPageStatus as LiveData<Boolean>

    private val _goToSplashScreenStatus = MutableLiveData<Boolean>()
    val goToSplashScreenStatus =  _goToSplashScreenStatus as LiveData<Boolean>

    private val _goToResetPasswordStatus = MutableLiveData<Boolean>()
    val goToResetPasswordStatus = _goToResetPasswordStatus as LiveData<Boolean>

    fun setGoToHomePageStatus(status: Boolean){
        _goToHomePageStatus.value = status
    }

    fun setGoToLoginPageStatus(status: Boolean){
        _goToLoginPageStatus.value = status
    }

    fun setGoToRegisterPageStatus(status: Boolean){
        _goToRegisterPageStatus.value = status
    }

    fun setGoToSplashScreenStatus(status: Boolean){
        _goToSplashScreenStatus.value = status
    }

    fun setGoToResetPasswordStatus(status: Boolean){
        _goToResetPasswordStatus.value = status
    }
}