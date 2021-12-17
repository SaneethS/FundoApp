package com.yml.fundo.networking.users

import com.yml.fundo.data.model.FirebaseUserDetails
import com.yml.fundo.networking.RetrofitClient

class UserService {
    private val retrofit = RetrofitClient.createRetrofit()

    suspend fun getUsers(): ArrayList<FirebaseUserDetails> {
        val usersApi = retrofit.create(UsersApi::class.java)
        val userResponse = usersApi.getUsers()
        val userList: ArrayList<FirebaseUserDetails> = ArrayList(userResponse.documents.map {
            FirebaseUserDetails(
                it.fields.name.stringValue,
                it.fields.email.stringValue,
                it.fields.mobileNo.stringValue
            )
        })
        return userList
    }
}