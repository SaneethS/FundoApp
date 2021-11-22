package com.yml.fundo.ui.home

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.auth.Authentication
import com.yml.fundo.data.service.DatabaseService
import com.yml.fundo.data.service.Storage
import com.yml.fundo.data.service.SyncDatabase
import com.yml.fundo.ui.wrapper.Notes
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {
    private val _userAvatarStatus = MutableLiveData<Bitmap>()
    val userAvatarStatus = _userAvatarStatus as LiveData<Bitmap>

    private val _getNewNotesStatus = MutableLiveData<ArrayList<Notes>>()
    val getNewNotesStatus = _getNewNotesStatus as LiveData<ArrayList<Notes>>

    private val _getArchiveNotesStatus = MutableLiveData<ArrayList<Notes>>()
    val getArchiveNotesStatus = _getArchiveNotesStatus as LiveData<ArrayList<Notes>>

    private val _getReminderNotesStatus = MutableLiveData<ArrayList<Notes>>()
    val getReminderNotesStatus = _getArchiveNotesStatus as LiveData<ArrayList<Notes>>

    private val _userDataStatus = MutableLiveData<User>()
    val userDataStatus = _userDataStatus as LiveData<User>

    private val _syncDataStatus = MutableLiveData<Boolean>()
    val syncDataStatus = _syncDataStatus as LiveData<Boolean>

    fun logoutFromHome(context: Context) {
        viewModelScope.launch {
            Authentication.logOut(context)
        }
    }

    fun setUserAvatar(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val status = Storage.setAvatar(bitmap)
                if (status) {
                    _userAvatarStatus.value = bitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUserAvatar() {
        viewModelScope.launch {
            val bitmap = Storage.getAvatar()
            if (bitmap != null) {
                _userAvatarStatus.value = bitmap
            }
        }
    }


    fun getNewNotes(context: Context) {
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getNewNoteFromDB()
            if (resultNotes != null) {
                _getNewNotesStatus.value = resultNotes
            }
        }
    }

    fun getArchivedNotes(context: Context) {
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getArchiveNotesFromDB()
            if (resultNotes != null) {
                _getArchiveNotesStatus.value = resultNotes
            }
        }
    }

    fun getReminderNotes(context: Context) {
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getReminderNotesFromDB()
            if (resultNotes != null) {
                _getReminderNotesStatus.value = resultNotes
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

    fun syncData(context: Context, user: User) {
        viewModelScope.launch {
            SyncDatabase(context).syncNow(user)
            _syncDataStatus.postValue(true)
        }
    }
}