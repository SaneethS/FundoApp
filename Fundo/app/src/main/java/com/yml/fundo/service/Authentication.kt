package com.yml.fundo.service

import android.util.Log
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.User

object Authentication {
    private var fauth:FirebaseAuth = Firebase.auth

    fun getCurrentUser() = fauth.currentUser

    fun registerEmailPassword(email: String, password:String, callback: (Boolean,FirebaseUser?) -> Unit){
        if(getCurrentUser() != null){
            callback(true,getCurrentUser())
        }
        fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("Authenticate","Sign up Successful")
                callback(true,getCurrentUser())
            }else{
                Log.i("Authenticate","Sign up failed")
                Log.i("Authenticate",it.exception.toString())
                callback(false, null)
            }
        }
    }

    fun loginEmailPassword(email: String, password: String,callback: (Boolean,FirebaseUser?) -> Unit){

        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("Authenticate","Sign in Successful")
                callback(true,getCurrentUser())
            }else{
                Log.i("Authenticate","Sign in failed")
                Log.i("Authenticate",it.exception.toString())
                callback(false, null)
            }
        }
    }

    fun signInWithFacebook(accessToken: AccessToken, callback: (FirebaseUser?) -> Unit){
        var credentials = FacebookAuthProvider.getCredential(accessToken.token)
        fauth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                var fbUser = getCurrentUser()
                var name = fbUser?.displayName.toString()
                var email = fbUser?.email.toString()
                var mobileNo = fbUser?.phoneNumber.toString()
                var user = User(name,email,mobileNo)
                Database.setToDatabase(user){}
                callback(getCurrentUser())
            }else{
                callback(null)
            }
        }
    }

    fun resetPassword(email:String, callback: (String)-> Unit){
        fauth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful){
                callback("Password reset sent to $email")
            }else{
                callback("Email not found!!")
            }
        }
    }

    fun logOut() {
        LoginManager.getInstance().logOut()
        fauth.signOut()
    }

}