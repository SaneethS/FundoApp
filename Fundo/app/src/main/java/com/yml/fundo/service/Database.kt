package com.yml.fundo.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.User
import com.yml.fundo.service.Database.database

object Database {
    val database:DatabaseReference = Firebase.database.reference

    fun getDatabase(user: User, callback: (Boolean)->Unit){
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true)
            }else{
                callback(false)
            }
        }
    }
}