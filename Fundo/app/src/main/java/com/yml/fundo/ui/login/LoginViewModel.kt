package com.yml.fundo.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.yml.fundo.ui.wrapper.User
import com.yml.fundo.auth.Authentication
import com.yml.fundo.common.SharedPref
import com.yml.fundo.data.service.DatabaseService
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginStatus = MutableLiveData<User>()
    val loginStatus = _loginStatus as LiveData<User>

    private val _facebookLoginStatus = MutableLiveData<User>()
    val facebookLoginStatus = _facebookLoginStatus as LiveData<User>

    fun loginWithEmailAndPassword(context: Context, email: String, password: String) {
        Authentication.loginEmailPassword(email, password) { user ->
            viewModelScope.launch {
                if (user?.loginStatus == true) {
                    if (user != null) {
                        val userDet = DatabaseService.getInstance(context).setUserToDatabase(user)
                        if (userDet != null) {
                            SharedPref.addUid(userDet.uid)
                            DatabaseService.getInstance(context).addCloudDataToLocalDB(userDet)
                            _loginStatus.postValue(userDet)
                        }
                    }
                } else {
                    _loginStatus.value = user
                }
            }
        }
    }

    fun facebookLoginWithUser(context: Context, accessToken: AccessToken) {
        Authentication.signInWithFacebook(accessToken) { user ->
            viewModelScope.launch {
                if (user?.loginStatus == true) {
                    if (user != null) {
                        val userDet =
                            DatabaseService.getInstance(context).setNewUserToDatabase(user)
                        if (userDet != null) {
                            SharedPref.addUid(user.uid)
                            DatabaseService.getInstance(context).addCloudDataToLocalDB(userDet)
                            _facebookLoginStatus.value = userDet
                        }
                    }
                } else {
                    _facebookLoginStatus.value = user
                }
            }
        }
    }
}