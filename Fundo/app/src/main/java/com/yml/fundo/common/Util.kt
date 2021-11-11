package com.yml.fundo.common

import com.yml.fundo.data.model.UserDetails

object Util {

    fun createUser(user: HashMap<*,*>):UserDetails{
        return UserDetails(user["name"].toString(), user["email"].toString(), user["mobileNo"].toString())
    }

    fun createUserInSharedPref(userDetails:UserDetails){
        SharedPref.addString("userEmail",userDetails.email)
        SharedPref.addString("userName", userDetails.name)
        SharedPref.addString("userMobile", userDetails.mobileNo)
    }

}