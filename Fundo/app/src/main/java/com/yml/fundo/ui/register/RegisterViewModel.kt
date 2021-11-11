package com.yml.fundo.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.auth.Authentication
import com.yml.fundo.data.service.DatabaseService
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus = _registerStatus as LiveData<Boolean>

    fun registerNewUser(user: User, password: String){
        Authentication.registerEmailPassword(user.email, password){
            if(it?.loginStatus == true){
                viewModelScope.launch{
                    DatabaseService.setToDatabase(user)
                    _registerStatus.value = it.loginStatus
                }
            }else{
                _registerStatus.value = it?.loginStatus
            }


            Log.i("db","user added to db")
        }
    }
}