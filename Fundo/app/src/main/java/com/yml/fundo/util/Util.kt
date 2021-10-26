package com.yml.fundo.util

import android.os.Bundle
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails

object Util {

    fun createUser(user: HashMap<*,*>):UserDetails{
        return UserDetails(user["name"].toString(), user["email"].toString(), user["mobileNo"].toString())
    }

}