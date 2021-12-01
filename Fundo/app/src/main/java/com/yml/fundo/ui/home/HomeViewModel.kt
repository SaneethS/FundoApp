package com.yml.fundo.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.auth.Authentication
import com.yml.fundo.data.service.DatabaseService
import com.yml.fundo.data.service.Storage
import com.yml.fundo.data.service.SyncDatabase
import com.yml.fundo.ui.wrapper.Note
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {
    private val _userAvatarStatus = MutableLiveData<Bitmap>()
    val userAvatarStatus = _userAvatarStatus as LiveData<Bitmap>

    private val _getNewNotesStatus = MutableLiveData<ArrayList<Note>>()
    val getNewNotesStatus = _getNewNotesStatus as LiveData<ArrayList<Note>>

    private val _getPagedNotesStatus = MutableLiveData<ArrayList<Note>>()
    val getPagedNotesStatus = _getPagedNotesStatus as LiveData<ArrayList<Note>>

    private val _getNoteCount = MutableLiveData<Int>()
    val getNoteCount = _getNoteCount as LiveData<Int>

    private val _getArchiveCount = MutableLiveData<Int>()
    val getArchiveCount = _getArchiveCount as LiveData<Int>

    private val _getReminderCount = MutableLiveData<Int>()
    val getReminderCount = _getReminderCount as LiveData<Int>

    private val _getArchiveNotesStatus = MutableLiveData<ArrayList<Note>>()
    val getArchiveNotesStatus = _getArchiveNotesStatus as LiveData<ArrayList<Note>>

    private val _getArchivePagedStatus = MutableLiveData<ArrayList<Note>>()
    val getArchivePagedStatus = _getArchivePagedStatus as LiveData<ArrayList<Note>>

    private val _getReminderNotesStatus = MutableLiveData<ArrayList<Note>>()
    val getReminderNotesStatus = _getReminderNotesStatus as LiveData<ArrayList<Note>>

    private val _getReminderPagedStatus = MutableLiveData<ArrayList<Note>>()
    val getReminderPagedStatus = _getReminderPagedStatus as LiveData<ArrayList<Note>>

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
            val resultNotes = DatabaseService.getInstance(context).getPagedNote(10, 0)
            if (resultNotes != null) {
                _getNewNotesStatus.value = resultNotes
            }
        }
    }

    fun getPagedNotes(context: Context, limit: Int, offset: Int){
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getPagedNote(limit, offset)
            if (resultNotes != null) {
                _getPagedNotesStatus.value = resultNotes
            }
        }
    }

    fun getNotesCount(context: Context) {
        viewModelScope.launch {
            val totalCount = DatabaseService.getInstance(context).getNoteCount()
            _getNoteCount.value = totalCount
        }
    }

    fun getArchiveCount(context: Context) {
        viewModelScope.launch {
            val totalCount = DatabaseService.getInstance(context).getArchiveCount()
            _getArchiveCount.value = totalCount
        }
    }

    fun getReminderCount(context: Context) {
        viewModelScope.launch {
            val totalCount = DatabaseService.getInstance(context).getReminderCount()
            _getReminderCount.value = totalCount
        }
    }

    fun getArchivedNotes(context: Context) {
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getArchivePaged(10,0)
            Log.i("VMService","$resultNotes")
            if (resultNotes != null) {
                _getArchiveNotesStatus.value = resultNotes
            }
        }
    }

    fun getArchivePaged(context: Context, limit: Int, offset: Int){
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getArchivePaged(limit, offset)
            if(resultNotes != null) {
                _getArchivePagedStatus.value = resultNotes
            }
        }
    }

    fun getReminderNotes(context: Context) {
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getReminderPaged(10,0)
            if (resultNotes != null) {
                _getReminderNotesStatus.value = resultNotes
            }
        }
    }

    fun getReminderPaged(context: Context, limit: Int, offset: Int){
        viewModelScope.launch {
            val resultNotes = DatabaseService.getInstance(context).getReminderPaged(limit, offset)
            if(resultNotes != null) {
                _getReminderPagedStatus.value = resultNotes
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