package com.yml.fundo.ui.login

import android.content.Context
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

    fun loginWithEmailAndPassword(context: Context,email: String, password: String){
        Authentication.loginEmailPassword(email, password){user->
            viewModelScope.launch {
                if (user != null) {
                    var userDet =DatabaseService.setToDatabase(context,user)
                    if(userDet != null){
                        SharedPref.addUid(userDet.uid)
                        DatabaseService.addCloudDataToLocalDB(context,userDet)
                        Log.i("Login","${userDet.uid}")
                        _loginStatus.value = userDet
                    }
                }
            }
        }
    }

    fun facebookLoginWithUser(context: Context,accessToken: AccessToken){
        Authentication.signInWithFacebook(accessToken){user->
            viewModelScope.launch {
                if (user != null) {
                    var userDet = DatabaseService.setNewUserToDatabase(context,user)
                    if(userDet != null){
                        SharedPref.addUid(user.uid)
                        DatabaseService.addCloudDataToLocalDB(context,userDet)
                        _facebookLoginStatus.value = userDet
                    }
                }
            }
        }
    }
}