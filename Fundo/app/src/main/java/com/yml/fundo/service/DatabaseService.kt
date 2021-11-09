package com.yml.fundo.service

import com.yml.fundo.model.Notes
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails
import com.yml.fundo.util.Util
import com.yml.fundo.wrapper.NotesKey

object DatabaseService {

    fun setToDatabase(user: User, callback: (Boolean)->Unit){
        FirebaseDatabase.setToDatabase(user){
            callback(it)
        }
    }

    fun getFromDatabase(callback: (Boolean) -> Unit){
        FirebaseDatabase.getFromDatabase(callback)
    }

    fun addNewNoteToDB(notes: Notes, callback: (Boolean) -> Unit){
        FirebaseDatabase.addNewNoteToDB(notes,callback)
    }

    fun getNewNoteFromDB(callback: (ArrayList<NotesKey>?) -> Unit){
        FirebaseDatabase.getNewNoteFromDB(callback)
    }

    fun updateNewNoteInDB(notes: NotesKey, callback: (Boolean) -> Unit){
        FirebaseDatabase.updateNewNoteInDB(notes,callback)
    }

    fun deleteNoteFromDB(notes: NotesKey, callback: (Boolean) -> Unit){
        FirebaseDatabase.deleteNoteFromDB(notes,callback)
    }
}