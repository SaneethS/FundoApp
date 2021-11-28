package com.yml.fundo.common

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.yml.fundo.R

class Notification: BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel1"
        const val TITLE = "title"
        const val CONTENT = "content"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_book_24)
            .setContentTitle(intent.getStringExtra(TITLE))
            .setContentText(intent.getStringExtra(CONTENT))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}