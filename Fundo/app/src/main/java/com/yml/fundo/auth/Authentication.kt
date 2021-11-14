package com.yml.fundo.auth

import android.content.Context
import android.util.Log
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.common.SharedPref
import com.yml.fundo.data.room.database.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Authentication {
    private var fauth: FirebaseAuth = Firebase.auth

    fun getCurrentUser() = fauth.currentUser

    fun registerEmailPassword(email: String, password: String, callback: (User?) -> Unit) {
        var user: User? = null

        fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Authenticate", "Sign up Successful")
                user = User(
                    name = getCurrentUser()?.displayName.toString(),
                    email = getCurrentUser()?.email.toString(),
                    mobileNo = getCurrentUser()?.phoneNumber.toString(),
                    loginStatus = true,
                    fUid = getCurrentUser()?.uid.toString()

                )
                callback(user)
            } else {
                Log.i("Authenticate", "Sign up failed")
                Log.i("Authenticate", it.exception.toString())
                user = User(name = "", email = "", mobileNo = "", loginStatus = false)
                callback(user)
            }
        }
    }

    fun loginEmailPassword(email: String, password: String, callback: (User?) -> Unit) {
        var user: User? = null

        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Authenticate", "Sign in Successful")
                user = User(
                    name = getCurrentUser()?.displayName.toString(),
                    email = getCurrentUser()?.email.toString(),
                    mobileNo = getCurrentUser()?.phoneNumber.toString(),
                    loginStatus = true,
                    fUid = getCurrentUser()?.uid.toString()
                )
                callback(user)
            } else {
                Log.i("Authenticate", "Sign in failed")
                Log.i("Authenticate", it.exception.toString())
                user = User(name = "", email = "", mobileNo = "", loginStatus = false)
                callback(user)
            }
        }
    }

    fun signInWithFacebook(accessToken: AccessToken, callback: (User?) -> Unit) {
        var credentials = FacebookAuthProvider.getCredential(accessToken.token)
        var user: User? = null
        fauth.signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                var fbUser = getCurrentUser()
                var name = fbUser?.displayName.toString()
                var email = fbUser?.email.toString()
                var mobileNo = fbUser?.phoneNumber.toString()
                user = User(name = name, email = email, mobileNo = mobileNo,
                    loginStatus = true, fUid = getCurrentUser()?.uid.toString())
                callback(user)
            } else {
                user = User(name = "", email = "", mobileNo = "", loginStatus = false)
                callback(null)
            }
        }
    }

    fun resetPassword(email: String, callback: (String) -> Unit) {
        fauth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback("Password reset sent to $email")
            } else {
                callback("Email not found!!")
            }
        }
    }

    suspend fun logOut(context: Context) {
        withContext(Dispatchers.IO){
            SharedPref.clearAll()
            LoginManager.getInstance().logOut()
            LocalDatabase.getInstance(context).clearAllTables()
            fauth.signOut()
        }
    }
}