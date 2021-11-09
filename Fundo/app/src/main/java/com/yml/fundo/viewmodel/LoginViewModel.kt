package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.yml.fundo.model.User
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.DatabaseService
import com.yml.fundo.service.FirebaseDatabase

class LoginViewModel: ViewModel() {
    private val _loginStatus = MutableLiveData<User>()
    val loginStatus = _loginStatus as LiveData<User>

    private val _facebookLoginStatus = MutableLiveData<User>()
    val facebookLoginStatus = _facebookLoginStatus as LiveData<User>

    fun loginWithEmailAndPassword(email: String, password: String){
        Authentication.loginEmailPassword(email, password){user->
            DatabaseService.getFromDatabase {
                _loginStatus.value = user
            }
        }
    }

    fun facebookLoginWithUser(accessToken: AccessToken){
        Authentication.signInWithFacebook(accessToken){user->
            if (user != null) {
                DatabaseService.setToDatabase(user){
                    _facebookLoginStatus.value = user
                }
            }
        }
    }
}