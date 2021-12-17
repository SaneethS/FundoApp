package com.yml.fundo.networking.users

import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {

    @GET("projects/fundo-612a1/databases/(default)/documents/users")
    suspend fun getUsers(): FirebaseUsersResponse

    @GET("projects/fundo-612a1/databases/(default)/documents/users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserDocument
}