package com.yml.fundo.util

import android.os.Bundle
import com.yml.fundo.model.User

object Util {

    fun createUser(user: User):Bundle{
        var bundle:Bundle = Bundle()
        bundle.putString("name",user.name)
        bundle.putString("email",user.email)
        bundle.putString("mobileNo",user.mobileNo)
        return bundle
    }

}