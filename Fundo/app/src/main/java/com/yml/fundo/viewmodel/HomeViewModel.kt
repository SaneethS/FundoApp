package com.yml.fundo.viewmodel

import androidx.lifecycle.ViewModel
import com.yml.fundo.service.Authentication

class HomeViewModel: ViewModel() {

    fun logoutFromHome(){
        Authentication.logOut()
    }
}