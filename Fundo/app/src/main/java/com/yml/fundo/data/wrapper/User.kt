package com.yml.fundo.data.wrapper

import com.yml.fundo.auth.Authentication

data class User(var name: String,var email:String,var mobileNo:String, var loginStatus:Boolean = Authentication.getCurrentUser() != null)
