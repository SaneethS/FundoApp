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

    fun getFromDatabase(uid:String, callback: (Boolean,Bundle?) -> Unit){
        var bundle:Bundle = Bundle()
        var result: DataSnapshot
        database.child("users").child(uid).get().addOnCompleteListener {
            if(it.isSuccessful){
                result = it.result!!
                bundle.putString("name",result.child("name").value.toString())
                bundle.putString("email",result.child("email").value.toString())
                bundle.putString("mobileNo",result.child("mobileNo").value.toString())
                callback(true,bundle)
            }else{
                callback(false,null)
            }

        }
    }

}