package com.yml.fundo.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.auth.Authentication
import com.yml.fundo.common.SharedPref
import com.yml.fundo.data.service.DatabaseService
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _loginStatus = MutableLiveData<User>()
    val loginStatus = _loginStatus as LiveData<User>

    private val _facebookLoginStatus = MutableLiveData<User>()
    val facebookLoginStatus = _facebookLoginStatus as LiveData<User>

    fun loginWithEmailAndPassword(email: String, password: String){
        Authentication.loginEmailPassword(email, password){user->
            viewModelScope.launch {
                if (user != null) {
                    var userDet =DatabaseService.setToDatabase(user)
                    if(userDet != null){
                        SharedPref.addUid(userDet.uid)
                        DatabaseService.addCloudDataToLocalDB(userDet)
                        Log.i("Login","${userDet.uid}")
                        _loginStatus.value = userDet
                    }
                }
            }
        }
    }

    fun facebookLoginWithUser(accessToken: AccessToken){
        Authentication.signInWithFacebook(accessToken){user->
            viewModelScope.launch {
                if (user != null) {
                    var userDet = DatabaseService.setNewUserToDatabase(user)
                    if(userDet != null){
                        SharedPref.addUid(user.uid)
                        DatabaseService.addCloudDataToLocalDB(userDet)
                        _facebookLoginStatus.value = userDet
                    }
                }
            }
        }
    }
}