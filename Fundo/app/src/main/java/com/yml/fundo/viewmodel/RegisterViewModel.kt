package com.yml.fundo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.model.User
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.DatabaseService
import com.yml.fundo.service.FirebaseDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus = _registerStatus as LiveData<Boolean>

    fun registerNewUser(user: User,password: String){
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