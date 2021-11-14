package com.yml.fundo.data.service

import android.content.Context
import android.util.Log
import com.yml.fundo.data.model.Notes
import com.yml.fundo.data.wrapper.User
import com.yml.fundo.data.wrapper.NotesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseService {
    private lateinit var sqlDb: SqLiteDatabase

    fun initSqliteDBService(context: Context) {
        sqlDb = SqLiteDatabase(context)
    }

    suspend fun setToDatabase(user: User): User? {
        return withContext(Dispatchers.IO) {
            try {
                val userFirebase = FirebaseDatabase.getFromDatabase(user.fUid)
                val userSql = sqlDb.setToDatabase(userFirebase)
//                FirebaseDatabase.setToDatabase(user)
                userSql
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun setNewUserToDatabase(user: User): User? {
        return withContext(Dispatchers.IO){
            try{
                val userFirebase = FirebaseDatabase.setToDatabase(user)
//                val userFirebase = FirebaseDatabase.getFromDatabase(user.fUid)
                val userSql = sqlDb.setToDatabase(userFirebase!!)
                userSql
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getFromDatabase(uid: Long): User? {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.getFromDatabase(uid)
//                FirebaseDatabase.getFromDatabase()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun addCloudDataToLocalDB(user: User) : Boolean {
        return withContext(Dispatchers.IO){
            val noteListFromCloud = FirebaseDatabase.getNewNoteFromDB(user)
            if (noteListFromCloud != null) {
                for( i in noteListFromCloud){
                    sqlDb.addNewNoteToDB(i)
                }
            }
            true
        }
    }

    suspend fun addNewNoteToDB(notes: NotesKey, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                var note = FirebaseDatabase.addNewNoteToDB(notes,user)
                sqlDb.addNewNoteToDB(note)

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

        }
    }

    suspend fun getNewNoteFromDB(): ArrayList<NotesKey>? {
        return withContext(Dispatchers.IO) {
            try {
                var notesList = sqlDb.getNewNoteFromDB()
//                var notesList = FirebaseDatabase.getNewNoteFromDB()
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    }

    suspend fun updateNewNoteInDB(notes: NotesKey, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.updateNewNoteInDB(notes)
                FirebaseDatabase.updateNewNoteInDB(notes, user)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

        }

    }

    suspend fun deleteNoteFromDB(notes: NotesKey, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sqlDb.deleteNoteFromDB(notes)
                FirebaseDatabase.deleteNoteFromDB(notes, user)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}