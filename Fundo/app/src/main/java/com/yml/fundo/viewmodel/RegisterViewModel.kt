package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.model.User
import com.yml.fundo.service.Authentication
import com.yml.fundo.service.Database

class RegisterViewModel: ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus = _registerStatus as LiveData<Boolean>

    fun registerNewUser(user: User,password: String){
        Authentication.registerEmailPassword(user.email, password){
            Database.setToDatabase(user){
                _registerStatus.value = it
            }

        }
    }
}