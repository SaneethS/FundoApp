package com.yml.fundo.model

import com.yml.fundo.service.Authentication

data class User(var name: String,var email:String,var mobileNo:String, var loginStatus:Boolean = Authentication.getCurrentUser() != null)
