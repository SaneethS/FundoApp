package com.yml.fundo.networking.users

import com.yml.fundo.data.model.FirebaseUserDetails
import com.yml.fundo.networking.RetrofitClient
import com.yml.fundo.ui.wrapper.User

class UserService {
    private val retrofit = RetrofitClient.createRetrofit()
    private val usersApi = retrofit.create(UsersApi::class.java)

    suspend fun getUsers(): ArrayList<FirebaseUserDetails> {
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

    suspend fun getUser(userId: String): User {
        val userResponse = usersApi.getUser(userId)
        val user: User = User(
            userResponse.fields.name.stringValue,
            userResponse.fields.email.stringValue,
            userResponse.fields.mobileNo.stringValue,
            fUid = userId
        )
        return user
    }
}