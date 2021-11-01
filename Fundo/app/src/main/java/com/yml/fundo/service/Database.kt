package com.yml.fundo.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.Notes
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails
import com.yml.fundo.util.Util

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

    fun getNewNoteFromDB(callback: (ArrayList<Notes>?) -> Unit){
        var uid = Authentication.getCurrentUser()?.uid.toString()
        database.child("note").child(uid).get().addOnCompleteListener {
            if(it.isSuccessful){
                var noteList = ArrayList<Notes>()
                var dataSnapshot = it.result

                if(dataSnapshot != null){
                    for(item in dataSnapshot.children){
                        var note = Notes(item.child("title").value.toString(), item.child("content").value.toString())
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
}