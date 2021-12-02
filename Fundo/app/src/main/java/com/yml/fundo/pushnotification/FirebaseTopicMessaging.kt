package com.yml.fundo.pushnotification

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

object FirebaseTopicMessaging {

    fun setTopicToSubscribe() {
        Firebase.messaging.subscribeToTopic("notes")
            .addOnCompleteListener { task ->
                var msg = "Success"
                if (!task.isSuccessful) {
                    msg = "Failed"
                }
                Log.d("Topic", msg)
            }
    }
}