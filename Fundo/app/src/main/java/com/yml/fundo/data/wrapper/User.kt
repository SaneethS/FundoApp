package com.yml.fundo.data.wrapper

import com.yml.fundo.auth.Authentication

data class User(
    var name: String,
    var email: String,
    var mobileNo: String,
    val fUid: String = "",
    var uid: Long = 0,
    var loginStatus: Boolean = Authentication.getCurrentUser() != null
)
