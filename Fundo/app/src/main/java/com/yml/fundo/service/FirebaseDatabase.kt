package com.yml.fundo.service

import com.google.firebase.FirebaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yml.fundo.model.Notes
import com.yml.fundo.model.User
import com.yml.fundo.model.UserDetails
import com.yml.fundo.util.Util
import com.yml.fundo.wrapper.NotesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

object FirebaseDatabase {
    private var database:DatabaseReference = Firebase.database.reference

    suspend fun setToDatabase(user: User):Boolean{
        var userDetails = UserDetails(user.name, user.email, user.mobileNo)
        return suspendCoroutine{    callback ->
            database.child("users").child(Authentication.getCurrentUser()?.uid.toString())
                .setValue(user).addOnCompleteListener {
                if(it.isSuccessful){
                    Util.createUserInSharedPref(userDetails)
                    callback.resumeWith(Result.success(true))

                }else{
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }
    }

    suspend fun getFromDatabase():Boolean{
        return suspendCoroutine{    callback->
            database.child("users").child(Authentication.getCurrentUser()?.uid.toString())
                .get().addOnCompleteListener {status ->
                if(status.isSuccessful){
                    status.result.also {
                        val user = Util.createUser(it?.value as HashMap<*, *>)
                        Util.createUserInSharedPref(user)
                        callback.resumeWith(Result.success(true))
                    }
                }else{
                    callback.resumeWith(Result.failure(status.exception!!))
                }
            }
        }

    }

    suspend fun addNewNoteToDB(notes: Notes): NotesKey{
        var uid = Authentication.getCurrentUser()?.uid.toString()
        return suspendCoroutine { callback ->
            val ref = database.child("note").child(uid).push()
                ref.setValue(notes).addOnCompleteListener {
                if(it.isSuccessful){
                    var notesKey = NotesKey(notes.title, notes.content, ref.key.toString())
                    callback.resumeWith(Result.success(notesKey))
                }else{
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }

    }

    suspend fun getNewNoteFromDB():ArrayList<NotesKey>?{
        var uid = Authentication.getCurrentUser()?.uid.toString()
        return suspendCoroutine { callback->
            database.child("note").child(uid).get().addOnCompleteListener {
                if(it.isSuccessful){
                    var noteList = ArrayList<NotesKey>()
                    var dataSnapshot = it.result

                    if(dataSnapshot != null){
                        for(item in dataSnapshot.children){
                            var note = NotesKey(item.child("title").value.toString(), item.child("content").value.toString(),item.key.toString())
                            noteList.add(note)
                        }
                        callback.resumeWith(Result.success(noteList))
                    }else{
                        callback.resumeWith(Result.failure(it.exception!!))
                    }
                }else{
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }

    }

    suspend fun updateNewNoteInDB(notes: NotesKey):Boolean{
        val notesMap = mapOf(
            "title" to notes.title,
            "content" to notes.content
        )
        var uid = Authentication.getCurrentUser()?.uid.toString()
        return suspendCoroutine { callback ->
            database.child("note").child(uid).child(notes.key).updateChildren(notesMap).addOnCompleteListener{
                if(it.isSuccessful){
                    callback.resumeWith(Result.success(true))
                }else{
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }
        }
    }

    suspend fun deleteNoteFromDB(notes: NotesKey):Boolean{
        var uid = Authentication.getCurrentUser()?.uid.toString()
        return suspendCoroutine { callback ->
            database.child("note").child(uid).child(notes.key).removeValue().addOnCompleteListener {
                if(it.isSuccessful){
                    callback.resumeWith(Result.success(true))
                }else{
                    callback.resumeWith(Result.failure(it.exception!!))
                }
            }

        }
    }
}