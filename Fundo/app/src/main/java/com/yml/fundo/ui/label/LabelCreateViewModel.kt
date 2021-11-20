package com.yml.fundo.ui.label

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yml.fundo.common.SharedPref
import com.yml.fundo.data.service.DatabaseService
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.User
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class LabelCreateViewModel : ViewModel() {
    private val _addNewLabelStatus = MutableLiveData<Label>()
    val addNewLabelStatus = _addNewLabelStatus as LiveData<Label>

    private val _userDataStatus = MutableLiveData<User>()
    val userDataStatus = _userDataStatus as LiveData<User>

    private val _getLabelStatus = MutableLiveData<ArrayList<Label>>()
    val getLabelStatus = _getLabelStatus as LiveData<ArrayList<Label>>

    private val _deleteLabelStatus = MutableLiveData<Label>()
    val deleteLabelStatus = _deleteLabelStatus as LiveData<Label>

    private val _updateLabelStatus = MutableLiveData<Label>()
    val updateLabelStatus = _updateLabelStatus as LiveData<Label>

    fun addNewLabel(context: Context, label: Label, user: User) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            label.dateModified = cal.time
            val label = DatabaseService.getInstance(context).addNewLabelToDB(label, user)
            if (label != null) {
                _addNewLabelStatus.postValue(label)
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

    fun getLabel(context: Context) {
        viewModelScope.launch {
            val userId = SharedPref.getId()
            val userDetails = DatabaseService.getInstance(context).getFromDatabase(userId)
            val label = DatabaseService.getInstance(context).getLabel(userDetails)
            if (label != null) {
                _getLabelStatus.value = label
            }
        }
    }

    fun deleteLabel(context: Context, label: Label) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            label.dateModified = cal.time
            val userId = SharedPref.getId()
            val userDetails = DatabaseService.getInstance(context).getFromDatabase(userId)
            val label = DatabaseService.getInstance(context).deleteLabel(label, userDetails)
            if (label != null) {
                _deleteLabelStatus.value = label
            }
        }
    }

    fun updateLabel(context: Context, label: Label) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            label.dateModified = cal.time
            val userId = SharedPref.getId()
            val userDetails = DatabaseService.getInstance(context).getFromDatabase(userId)
            val label =
                userDetails?.let { DatabaseService.getInstance(context).updateLabel(label, it) }
            if (label != null) {
                _updateLabelStatus.value = label
            }
        }
    }
}