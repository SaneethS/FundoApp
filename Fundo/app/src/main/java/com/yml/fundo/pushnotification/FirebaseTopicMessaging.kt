package com.yml.fundo.pushnotification

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

object FirebaseTopicMessaging {

    fun setTopic() {
        Firebase.messaging.subscribeToTopic("general")
            .addOnCompleteListener { task ->
                var msg = "Success"
                if (!task.isSuccessful) {
                    msg = "Failed"
                }
                Log.d(TAG, msg)
            }
    }
}