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
    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    private val _goToRegisterPageStatus = MutableLiveData<Boolean>()
    private val _goToSplashScreenStatus = MutableLiveData<Boolean>()
    private val _goToResetPasswordStatus = MutableLiveData<Boolean>()
    private val _loginStatus = MutableLiveData<User>()
    private val _registerStatus = MutableLiveData<User>()
    private val _facebookLoginStatus = MutableLiveData<User>()
    private val _resetPasswordStatus = MutableLiveData<String>()

    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>
    val goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>
    val goToRegisterPageStatus = _goToRegisterPageStatus as LiveData<Boolean>
    val goToSplashScreenStatus =  _goToSplashScreenStatus as LiveData<Boolean>
    val goToResetPasswordStatus = _goToResetPasswordStatus as LiveData<Boolean>
    val loginStatus = _loginStatus as LiveData<User>
    val registerStatus = _registerStatus as LiveData<User>
    val facebookLoginStatus = _facebookLoginStatus as LiveData<User>
    val resetPasswordStatus = _resetPasswordStatus as LiveData<String>

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

    fun loginWithEmailAndPassword(email: String, password: String){
        Authentication.loginEmailPassword(email, password){
            _loginStatus.value = it
        }
    }

    fun registerNewUser(email: String,password: String){
        Authentication.registerEmailPassword(email, password){
            _registerStatus.value = it
        }
    }

    fun facebookLoginWithUser(accessToken: AccessToken){
        Authentication.signInWithFacebook(accessToken){
            _facebookLoginStatus.value = it
        }
    }

    fun resetPasswordOfUser(email: String){
        Authentication.resetPassword(email){
            _resetPasswordStatus.value = it
        }
    }
}