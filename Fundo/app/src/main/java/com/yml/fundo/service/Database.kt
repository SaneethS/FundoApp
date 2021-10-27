package com.yml.fundo.service

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails
import com.yml.fundo.service.Database.database
import com.yml.fundo.util.Util

object Database {
    private var database:DatabaseReference = Firebase.database.reference

    fun setToDatabase(user: User, callback: (Boolean)->Unit){
        var userDetails = UserDetails(user.name, user.email, user.mobileNo)
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                Util.createUserInSharedPref(userDetails)
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun getFromDatabase(callback: (Boolean) -> Unit){
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).get().addOnCompleteListener {status ->
            if(status.isSuccessful){
                status.result.also {
                    val user = Util.createUser(it?.value as HashMap<*, *>)
                    Util.createUserInSharedPref(user)
                    callback(true)
                }
            }else{
                callback(false)
            }

        }
    }

}