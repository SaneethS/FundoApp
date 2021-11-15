package com.yml.fundo.data.service

import android.content.Context
import android.util.Log
import com.yml.fundo.common.NetworkService
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

    suspend fun setToDatabase(context: Context,user: User): User? {
        return withContext(Dispatchers.IO) {
            try {
                val userFirebase = FirebaseDatabase.getFromDatabase(user.fUid)
                val userSql = sqlDb.setToDatabase(userFirebase)
                userSql
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun setNewUserToDatabase(context: Context,user: User): User? {
        return withContext(Dispatchers.IO){
            try{
                val userFirebase = FirebaseDatabase.setToDatabase(user)
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
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    suspend fun addCloudDataToLocalDB(context: Context,user: User) : Boolean {
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

    suspend fun addNoteToLocalDb(notes: NotesKey):Boolean{
        return withContext(Dispatchers.IO){
            try {
                sqlDb.addNewNoteToDB(notes, false)
                true
            }catch (e:Exception){
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun addNewNoteToDB(context: Context,notes: NotesKey, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if(NetworkService.isNetworkAvailable(context)){
                    var note = FirebaseDatabase.addNewNoteToDB(notes,user)
                    sqlDb.addNewNoteToDB(note)
                }else{
                    sqlDb.addNewNoteToDB(notes, false)
                }
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
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getNewNoteFromCloud(user: User): ArrayList<NotesKey>? {
        return withContext(Dispatchers.IO) {
            try {
                var notesList = FirebaseDatabase.getNewNoteFromDB(user)
                notesList
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }



    suspend fun updateNewNoteInDB(context: Context,notes: NotesKey, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if(NetworkService.isNetworkAvailable(context)){
                    sqlDb.updateNewNoteInDB(notes)
                    FirebaseDatabase.updateNewNoteInDB(notes, user)
                }else{
                    sqlDb.updateNewNoteInDB(notes, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun deleteNoteFromDB(context: Context,notes: NotesKey, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkService.isNetworkAvailable(context)){
                    sqlDb.deleteNoteFromDB(notes)
                    FirebaseDatabase.deleteNoteFromDB(notes, user)
                }else{
                    sqlDb.deleteNoteFromDB(notes, false)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getOpCode(notes: NotesKey):Int{
        return withContext(Dispatchers.IO){
            return@withContext sqlDb.getOpCode(notes)
        }
    }

    suspend fun clearNoteAndOperation(){
        sqlDb.clearNoteAndOperation()
    }
}