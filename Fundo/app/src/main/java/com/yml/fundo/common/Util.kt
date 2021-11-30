package com.yml.fundo.common

import android.os.Bundle
import com.yml.fundo.data.model.FirebaseUserDetails
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.ui.wrapper.Note

object Util {

    fun createUser(user: HashMap<*, *>): FirebaseUserDetails {
        return FirebaseUserDetails(
            user["name"].toString(),
            user["email"].toString(),
            user["mobileNo"].toString()
        )
    }

    fun createUserInSharedPref(firebaseUserDetails: FirebaseUserDetails) {
        SharedPref.addString("userEmail", firebaseUserDetails.email)
        SharedPref.addString("userName", firebaseUserDetails.name)
        SharedPref.addString("userMobile", firebaseUserDetails.mobileNo)
    }

    fun setToNotesBundle(note: Note): Bundle {
        val bundle = Bundle()
        val dateTime = DateTypeConverter().fromOffsetDateTime(note.dateModified)
        bundle.putString("title", note.title)
        bundle.putString("notes", note.content)
        bundle.putString("key", note.key)
        bundle.putLong("id", note.id)
        bundle.putString("dateModified", dateTime)
        bundle.putBoolean("archived", note.archived)
        bundle.putString("reminder", DateTypeConverter().fromOffsetDateTime(note.reminder))
        return bundle
    }
}