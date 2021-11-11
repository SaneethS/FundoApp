package com.yml.fundo.service

import com.yml.fundo.model.Notes
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails
import com.yml.fundo.util.Util
import com.yml.fundo.wrapper.NotesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseService {

    suspend fun setToDatabase(user: User){
        withContext(Dispatchers.IO){
            try{
                FirebaseDatabase.setToDatabase(user)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

    suspend fun getFromDatabase(){
        withContext(Dispatchers.IO){
            try {
                FirebaseDatabase.getFromDatabase()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun addNewNoteToDB(notes: Notes):Boolean{
        return withContext(Dispatchers.IO){
            try {
                FirebaseDatabase.addNewNoteToDB(notes)
                true
            }catch (e: Exception){
                e.printStackTrace()
                false
            }

        }
    }

    suspend fun getNewNoteFromDB():ArrayList<NotesKey>?{
        return withContext(Dispatchers.IO){
            try {
                var notesList = FirebaseDatabase.getNewNoteFromDB()
                notesList
            }catch (e:Exception){
                e.printStackTrace()
                null
            }
        }

    }

    suspend fun updateNewNoteInDB(notes: NotesKey):Boolean{
        return withContext(Dispatchers.IO){
            try {
                FirebaseDatabase.updateNewNoteInDB(notes)
                true
            }catch (e: Exception){
                e.printStackTrace()
                false
            }

        }

    }

    suspend fun deleteNoteFromDB(notes: NotesKey):Boolean{
        return withContext(Dispatchers.IO){
            try {
                FirebaseDatabase.deleteNoteFromDB(notes)
                true
            }catch (e: Exception){
                e.printStackTrace()
                false
            }
        }
    }
}