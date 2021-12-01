package com.yml.fundo.common

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yml.fundo.R
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.ui.MainActivity
import com.yml.fundo.ui.wrapper.Note

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "channel1"

class NotifyWorker(private val context: Context, private val workerParams: WorkerParameters):
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString(TITLE)
        val content = inputData.getString(CONTENT)
        val dateModified = DateTypeConverter().toOffsetDateTime(inputData.getString(DATE_MODIFIED))
        val fNid = inputData.getString(F_NID)
        val nid = inputData.getLong(NID, 0L)
        val archived = inputData.getBoolean(ARCHIVED, false)
        val reminder = DateTypeConverter().toOffsetDateTime(inputData.getString(REMINDER))
        val notes = Note(title.toString(), content.toString(), dateModified, fNid.toString()
                            ,nid, archived, reminder)

        val newIntent = Intent(context, MainActivity::class.java)
        newIntent.putExtra("destination", "home")
        newIntent.putExtra("notes", notes)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_book_24)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(context, 0, newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
        return Result.success()
    }
}