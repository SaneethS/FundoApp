package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.model.Notes
import com.yml.fundo.service.Database
import com.yml.fundo.wrapper.NotesKey

class NoteViewModel: ViewModel() {
    private val _addNewNoteStatus = MutableLiveData<Boolean>()
    val addNewNoteStatus = _addNewNoteStatus as LiveData<Boolean>

    private val _updateNoteStatus = MutableLiveData<Boolean>()
    val updateNoteStatus = _updateNoteStatus as LiveData<Boolean>

    private val _deleteNoteStatus = MutableLiveData<Boolean>()
    val deleteNoteStatus = _deleteNoteStatus as LiveData<Boolean>

    fun addNewNote(notes: Notes){
        Database.addNewNoteToDB(notes){
            if(it){
                _addNewNoteStatus.value = it
            }
        }
    }

    fun updateNotes(notes: NotesKey){
        Database.updateNewNoteInDB(notes){
            _updateNoteStatus.value = it
        }
    }

    fun deleteNotes(notes: NotesKey){
        Database.deleteNoteFromDB(notes){
            _deleteNoteStatus.value = it
        }
    }
}