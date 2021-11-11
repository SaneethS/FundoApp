package com.yml.fundo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.auth.Authentication
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
                DatabaseService.getFromDatabase()
                _loginStatus.value = user
            }
        }
    }

    fun facebookLoginWithUser(accessToken: AccessToken){
        Authentication.signInWithFacebook(accessToken){user->
            viewModelScope.launch {
                if (user != null) {
                    DatabaseService.setToDatabase(user)
                    _facebookLoginStatus.value = user

                }
            }
        }
    }
}