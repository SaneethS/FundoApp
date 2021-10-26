package com.yml.fundo.service

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.User
import com.yml.fundo.service.Database.database

object Database {
    private var database:DatabaseReference = Firebase.database.reference

    fun setToDatabase(user: User, callback: (Boolean)->Unit){
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun getFromDatabase(callback: (HashMap<*,*>) -> Unit){
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).get().addOnCompleteListener {status ->
            if(!status.isSuccessful){

            }else{
                status.result.also {
                    callback(it?.value as HashMap<*, *>)
                }
            }

        }
    }

}