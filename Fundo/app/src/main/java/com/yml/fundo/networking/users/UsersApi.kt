package com.yml.fundo.networking.users

import retrofit2.http.GET

interface UsersApi {

    @GET("projects/fundo-612a1/databases/(default)/documents/users")
    suspend fun getUsers(): FirebaseUsersResponse
}