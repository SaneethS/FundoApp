package com.yml.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yml.fundo.model.Notes
import com.yml.fundo.service.Database

class NoteViewModel: ViewModel() {
    private val _addNewNoteStatus = MutableLiveData<Boolean>()
    val addNewNoteStatus = _addNewNoteStatus as LiveData<Boolean>

    fun addNewNote(notes: Notes){
        Database.addNewNoteToDB(notes){
            if(it){
                _addNewNoteStatus.value = it
            }
        }
    }
}