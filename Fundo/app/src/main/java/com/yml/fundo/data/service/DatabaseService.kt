package com.yml.fundo.data.service

import android.content.Context
import com.yml.fundo.common.NetworkService
import com.yml.fundo.ui.wrapper.User
import com.yml.fundo.ui.wrapper.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseService(val context: Context) {
    private var sqlDb: SqLiteDatabase = SqLiteDatabase(context)

    companion object{
        private val instance: DatabaseService? by lazy {null}

        fun getInstance(context: Context):DatabaseService = instance?: DatabaseService(context)
    }

    suspend fun setToDatabase(user: User): User? {
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

    suspend fun setNewUserToDatabase(user: User): User? {
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

    suspend fun addNoteToLocalDb(notes: Notes):Boolean{
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

    suspend fun addNewNoteToDB(notes: Notes, user: User): Boolean {
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

    suspend fun getNewNoteFromDB(): ArrayList<Notes>? {
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

    suspend fun getNewNoteFromCloud(user: User): ArrayList<Notes>? {
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



    suspend fun updateNewNoteInDB(notes: Notes, user: User): Boolean {
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

    suspend fun deleteNoteFromDB(notes: Notes, user: User): Boolean {
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

    suspend fun getOpCode(notes: Notes):Int{
        return withContext(Dispatchers.IO){
            return@withContext sqlDb.getOpCode(notes)
        }
    }

    suspend fun clearNoteAndOperation(){
        sqlDb.clearNoteAndOperation()
    }

    suspend fun clearAllTables(){
        sqlDb.clearAllTables()
    }
}