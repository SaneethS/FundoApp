package com.yml.fundo.ui.note

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.data.service.DatabaseService
import com.yml.fundo.ui.wrapper.Notes
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel : ViewModel() {
    private val _addNewNoteStatus = MutableLiveData<Boolean>()
    val addNewNoteStatus = _addNewNoteStatus as LiveData<Boolean>

    private val _updateNoteStatus = MutableLiveData<Boolean>()
    val updateNoteStatus = _updateNoteStatus as LiveData<Boolean>

    private val _deleteNoteStatus = MutableLiveData<Boolean>()
    val deleteNoteStatus = _deleteNoteStatus as LiveData<Boolean>

    private val _userDataStatus = MutableLiveData<User>()
    val userDataStatus = _userDataStatus as LiveData<User>

    fun addNewNote(context: Context, notes: Notes, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            notes.dateModified = cal.time
            val result = DatabaseService.getInstance(context).addNewNoteToDB(notes, user)
            if (result) {
                _addNewNoteStatus.value = result
            }
        }
    }

    fun updateNotes(context: Context, notes: Notes, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            notes.dateModified = cal.time
            val status = DatabaseService.getInstance(context).updateNewNoteInDB(notes, user)
            if (status) {
                _updateNoteStatus.value = status
            }
        }
    }

    fun deleteNotes(context: Context, notes: Notes, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            notes.dateModified = cal.time
            val status = DatabaseService.getInstance(context).deleteNoteFromDB(notes, user)
            if (status) {
                _deleteNoteStatus.value = status
            }
        }
    }

    fun getUserInfo(context: Context, uid: Long) {
        viewModelScope.launch {
            val userData = DatabaseService.getInstance(context).getFromDatabase(uid)
            if (userData != null) {
                _userDataStatus.postValue(userData)
            }
        }
    }
}