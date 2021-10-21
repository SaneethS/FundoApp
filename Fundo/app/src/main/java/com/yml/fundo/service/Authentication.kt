package com.yml.fundo.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Authentication {
    private var fauth:FirebaseAuth = Firebase.auth

    fun getCurrentUser() = fauth.currentUser

    fun registerEmailPassword(email: String, password:String, callback: (FirebaseUser?) -> Unit){
        fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("Authenticate","Sign up Successful")
                callback(getCurrentUser())
            }else{
                Log.i("Authenticate","Sign up failed")
                Log.i("Authenticate",it.exception.toString())
                callback(null)
            }
        }
    }

    fun loginEmailPassword(email: String, password: String,callback: (FirebaseUser?) -> Unit){
        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("Authenticate","Sign in Successful")
                callback(getCurrentUser())
            }else{
                Log.i("Authenticate","Sign in failed")
                Log.i("Authenticate",it.exception.toString())
                callback(null)
            }
        }
    }


    fun logOut() = fauth.signOut()

}