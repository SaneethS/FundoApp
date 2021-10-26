package com.yml.fundo.util

import android.os.Bundle
import com.yml.fundo.model.User

object Util {

    fun createUser(user: HashMap<*,*>):User{
        return User(user["name"].toString(), user["email"].toString(), user["mobileNo"].toString())
    }

}