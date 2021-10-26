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
import com.yml.fundo.model.UserDetails

object Authentication {
    private var fauth:FirebaseAuth = Firebase.auth

    fun getCurrentUser() = fauth.currentUser

    fun registerEmailPassword(email: String, password:String, callback: (User?) -> Unit){
        var user:User? = null
        if(getCurrentUser() != null){
            user = User(getCurrentUser()?.displayName.toString(), getCurrentUser()?.email.toString(), getCurrentUser()?.phoneNumber.toString(), true)
            callback(user)
        }
        fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("Authenticate","Sign up Successful")
                user = User(getCurrentUser()?.displayName.toString(), getCurrentUser()?.email.toString(), getCurrentUser()?.phoneNumber.toString(), true)
                callback(user)
            }else{
                Log.i("Authenticate","Sign up failed")
                Log.i("Authenticate",it.exception.toString())
                user = User("", "", "", false)
                callback(user)
            }
        }
    }

    fun loginEmailPassword(email: String, password: String,callback: (User?) -> Unit){
        var user:User? = null
        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("Authenticate","Sign in Successful")
                user = User(getCurrentUser()?.displayName.toString(), getCurrentUser()?.email.toString(), getCurrentUser()?.phoneNumber.toString(), true)
                callback(user)
            }else{
                Log.i("Authenticate","Sign in failed")
                Log.i("Authenticate",it.exception.toString())
                user = User("", "", "", false)
                callback(user)
            }
        }
    }

    fun signInWithFacebook(accessToken: AccessToken, callback: (User?) -> Unit){
        var credentials = FacebookAuthProvider.getCredential(accessToken.token)
        var user:User? = null
        fauth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                var fbUser = getCurrentUser()
                var name = fbUser?.displayName.toString()
                var email = fbUser?.email.toString()
                var mobileNo = fbUser?.phoneNumber.toString()
                user = User(name,email,mobileNo,true)
                val userDetails = UserDetails(name, email, mobileNo)
                Database.setToDatabase(userDetails!!){}
                callback(user)
            }else{
                user = User("","","",false)
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