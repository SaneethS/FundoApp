package com.yml.fundo.common

import com.yml.fundo.data.model.FirebaseUserDetails

object Util {

    fun createUser(user: HashMap<*, *>): FirebaseUserDetails {
        return FirebaseUserDetails(
            user["name"].toString(),
            user["email"].toString(),
            user["mobileNo"].toString()
        )
    }

    fun createUserInSharedPref(firebaseUserDetails: FirebaseUserDetails) {
        SharedPref.addString("userEmail", firebaseUserDetails.email)
        SharedPref.addString("userName", firebaseUserDetails.name)
        SharedPref.addString("userMobile", firebaseUserDetails.mobileNo)
    }
}