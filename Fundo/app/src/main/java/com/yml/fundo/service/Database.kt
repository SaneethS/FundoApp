package com.yml.fundo.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.Notes
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails
import com.yml.fundo.util.Util
import com.yml.fundo.wrapper.NotesKey

object Database {
    private var database:DatabaseReference = Firebase.database.reference

    fun setToDatabase(user: User, callback: (Boolean)->Unit){
        var userDetails = UserDetails(user.name, user.email, user.mobileNo)
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                Util.createUserInSharedPref(userDetails)
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun getFromDatabase(callback: (Boolean) -> Unit){
        database.child("users").child(Authentication.getCurrentUser()?.uid.toString()).get().addOnCompleteListener {status ->
            if(status.isSuccessful){
                status.result.also {
                    val user = Util.createUser(it?.value as HashMap<*, *>)
                    Util.createUserInSharedPref(user)
                    callback(true)
                }
            }else{
                callback(false)
            }
        }
    }

    fun addNewNoteToDB(notes: Notes, callback: (Boolean) -> Unit){
        var uid = Authentication.getCurrentUser()?.uid.toString()
        database.child("note").child(uid).push().setValue(notes).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true)
            }else{
                callback(false)
            }
        }

    }

    fun getNewNoteFromDB(callback: (ArrayList<NotesKey>?) -> Unit){
        var uid = Authentication.getCurrentUser()?.uid.toString()
        database.child("note").child(uid).get().addOnCompleteListener {
            if(it.isSuccessful){
                var noteList = ArrayList<NotesKey>()
                var dataSnapshot = it.result

                if(dataSnapshot != null){
                    for(item in dataSnapshot.children){
                        var note = NotesKey(item.child("title").value.toString(), item.child("content").value.toString(),item.key.toString())
                        noteList.add(note)
                    }
                    callback(noteList)
                }else{
                    callback(null)
                }
            }else{
                callback(null)
            }
        }
    }

    fun updateNewNoteInDB(notes: NotesKey, callback: (Boolean) -> Unit){
        val notesMap = mapOf(
            "title" to notes.title,
            "content" to notes.content
        )
        var uid = Authentication.getCurrentUser()?.uid.toString()
        database.child("note").child(uid).child(notes.key).updateChildren(notesMap).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun deleteNoteFromDB(notes: NotesKey, callback: (Boolean) -> Unit){
        var uid = Authentication.getCurrentUser()?.uid.toString()
        database.child("note").child(uid).child(notes.key).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true)
            }else{
                callback(false)
            }
        }
    }
}