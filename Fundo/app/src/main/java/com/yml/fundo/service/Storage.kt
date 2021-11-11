package com.yml.fundo.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.facebook.FacebookCallback
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import kotlin.coroutines.suspendCoroutine

object Storage {
    private val storage = Firebase.storage.reference
    private val image = storage.child("images")

    suspend fun setAvatar(bitmap: Bitmap):Boolean{
        return suspendCoroutine {   callback->
            val userImage = image.child("users").child(Authentication.getCurrentUser()?.uid.toString()).child("avatar.webp")
            val byteArray = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP,80,byteArray)
            val data = byteArray.toByteArray()

            val upload = userImage.putBytes(data)

            upload.addOnCompleteListener{
                if(it.isSuccessful){
                    callback.resumeWith(Result.success(true))
                }else{
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }

    }

    suspend fun getAvatar():Bitmap?{
        return suspendCoroutine {callback ->
            val userImage = image.child("users").child(Authentication.getCurrentUser()?.uid.toString()).child("avatar.webp")
            userImage.getBytes(5000000).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                callback.resumeWith(Result.success(bitmap))
            }.addOnFailureListener {
                callback.resumeWith(Result.success(null))
            }
        }
    }
}