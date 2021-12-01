package com.yml.fundo.ui.note

import android.app.*
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yml.fundo.R
import com.yml.fundo.common.*
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.databinding.NotePageBinding
import com.yml.fundo.ui.SharedViewModel
import com.yml.fundo.ui.wrapper.Note
import com.yml.fundo.ui.wrapper.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NoteFragment : Fragment(R.layout.note_page) {
    private lateinit var binding: NotePageBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var noteViewModel: NoteViewModel
    private var reminder: Date? = null
    private var noteTitle: String? = null
    private var noteContent: String? = null
    private var noteKey: String = ""
    private var bundleDateModified: Date? = null
    private var bundleNoteId: Long? = null
    private var bundleArchived: Boolean? = null
    private var bundleReminder: Date? = null
    private var userId: Long = 0L
    private var currentUser: User =
        User(name = "Name", email = "EmailID", mobileNo = "MobileNumber")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NotePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        userId = SharedPref.getId()
        noteViewModel.getUserInfo(requireContext(), userId)

        binding.backButton.setOnClickListener {
            if(bundleArchived == true) {
                sharedViewModel.setGoToArchivedNotePageStatus(true)
            }else{
                sharedViewModel.setGoToHomePageStatus(true)
            }
        }

        binding.saveNoteButton.setOnClickListener {
            saveNote()
        }

        binding.deleteButton.setOnClickListener {
            if (bundleNoteId == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.create_a_note_toast),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                deleteNote()
            }
        }

        noteViewModel.addNewNoteStatus.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.setGoToHomePageStatus(true)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.note_not_saved_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        noteContents()
        checkVisibility()

        noteViewModel.updateNoteStatus.observe(viewLifecycleOwner) {
            if (it) {
                if(bundleArchived == true) {
                    sharedViewModel.setGoToArchivedNotePageStatus(true)
                }else {
                    sharedViewModel.setGoToHomePageStatus(true)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.update_not_succesful_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        noteViewModel.deleteNoteStatus.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.setGoToHomePageStatus(true)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.deletion_failed_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        noteViewModel.userDataStatus.observe(viewLifecycleOwner) {
            currentUser = it
        }

        archiveNotes()
        reminderNotes()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun reminderNotes() {
        if(bundleReminder != null) {
            binding.reminderLayout.visibility = View.VISIBLE
            val formatter = SimpleDateFormat("dd MMM, hh:mm aa")
            val date = formatter.format(bundleReminder)
            binding.reminderTextView.text = date
        }
        binding.reminderButton.setOnClickListener {
            val currentDateTime = Calendar.getInstance()
            val startYear = currentDateTime.get(Calendar.YEAR)
            val startMonth = currentDateTime.get(Calendar.MONTH)
            val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
            val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentDateTime.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, month, day ->
                    TimePickerDialog(requireContext(), { _, hour, minute ->
                        val selectDateTime = Calendar.getInstance()
                        selectDateTime.set(year, month, day, hour, minute, 0)
                        reminder = selectDateTime.time
                        binding.reminderLayout.visibility = View.VISIBLE
                        val formatter = SimpleDateFormat("dd MMM, hh:mm aa")
                        val date = formatter.format(reminder)
                        binding.reminderTextView.text = date
                        if(reminder != null) {
                            val title = binding.titleText.text.toString()
                            val content = binding.noteText.text.toString()
                            val note =
                                Note(title, content, dateModified = bundleDateModified, noteKey,
                                    bundleNoteId!!, archived = bundleArchived!!, reminder = reminder)
                            noteViewModel.updateNotes(requireContext(), note, currentUser)
                            scheduleNotification(note)
                        }
                    },
                        startHour,
                        startMinute,
                        false
                    ).show()
                },
                startYear,
                startMonth,
                startDay
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding.reminderLayout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
                .setMessage("Do you want to delete the reminder?")
                .setPositiveButton("Yes") {
                    _, _ ->
                    val title = binding.titleText.text.toString()
                    val content = binding.noteText.text.toString()
                    val note =
                        Note(title, content, dateModified = bundleDateModified, noteKey,
                            bundleNoteId!!, archived = bundleArchived!!, reminder = null)
                    noteViewModel.updateNotes(requireContext(), note, currentUser)
                    binding.reminderLayout.visibility = View.GONE
                    scheduleNotification(note, cancel = true)
                }
                .setNegativeButton("No") {
                    _, _  ->
                }.create()

            alertDialog.show()
        }
    }

    private fun archiveNotes() {
        if(bundleArchived == false) {
            binding.archiveButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                R.drawable.archive_notepage))
        }else {
            binding.archiveButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                R.drawable.unarchive_notepage))
        }
        binding.archiveButton.setOnClickListener {
            if(bundleArchived == false){
                val title = binding.titleText.text.toString()
                val content = binding.noteText.text.toString()
                val note =
                    Note(title, content, dateModified = bundleDateModified, noteKey,
                        bundleNoteId!!, archived = true, reminder = bundleReminder)
                noteViewModel.updateNotes(requireContext(), note, currentUser)

            }else if(bundleArchived== true) {
                val title = binding.titleText.text.toString()
                val content = binding.noteText.text.toString()
                val note =
                    Note(title, content, dateModified = bundleDateModified, noteKey,
                        bundleNoteId!!, archived = false, reminder = bundleReminder)
                noteViewModel.updateNotes(requireContext(), note, currentUser)
            }
        }
    }

    private fun checkVisibility() {
        if(bundleNoteId == null){
            binding.deleteButton.visibility = View.GONE
            binding.archiveButton.visibility = View.GONE
            binding.reminderButton.visibility = View.GONE
        }else{
            binding.deleteButton.visibility = View.VISIBLE
            binding.archiveButton.visibility =  View.VISIBLE
            binding.reminderButton.visibility = View.VISIBLE
        }
    }

    private fun deleteNote() {
        val title = binding.titleText.text.toString()
        val content = binding.noteText.text.toString()
        if (title.isNotEmpty() || content.isNotEmpty()) {
            val note =
                Note(title, content, dateModified = bundleDateModified, noteKey, bundleNoteId!!)
            noteViewModel.deleteNotes(requireContext(), note, currentUser)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.deletion_not_posssible_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun noteContents() {
        val dateTime = DateTypeConverter().toOffsetDateTime(arguments?.getString("dateModified"))
        val reminderToDate = DateTypeConverter().toOffsetDateTime(arguments?.getString("reminder"))
        noteTitle = arguments?.getString("title")
        noteContent = arguments?.getString("notes")
        noteKey = arguments?.getString("key").toString()
        bundleDateModified = dateTime
        bundleNoteId = arguments?.getLong("id")
        bundleArchived = arguments?.getBoolean("archived")
        bundleReminder = reminderToDate
        binding.titleText.setText(noteTitle)
        binding.noteText.setText(noteContent)
    }

    private fun saveNote() {
        val title = binding.titleText.text.toString()
        val content = binding.noteText.text.toString()
        if (bundleNoteId == null) {
            val notes = Note(title, content, dateModified = null)
            Log.i("NoteCurrent", "$currentUser")
            noteViewModel.addNewNote(requireContext(), notes, currentUser)
        } else {
            val note =
                Note(title, content, dateModified = bundleDateModified, noteKey, bundleNoteId!!,
                bundleArchived!!, bundleReminder)
            noteViewModel.updateNotes(requireContext(), note, currentUser)
            Toast.makeText(
                requireContext(),
                getString(R.string.updated_successfully_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Fundoo notes notification channel"
        val desc = "Fundoo notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        val notificationManager = requireContext()
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(note: Note, cancel: Boolean = false) {
        val inputData = Data.Builder()
            .putString(TITLE, note.title)
            .putString(CONTENT, note.content)
            .putString(F_NID, note.key)
            .putLong(NID, note.id)
            .putBoolean(ARCHIVED, note.archived)
            .putString(DATE_MODIFIED, DateTypeConverter().fromOffsetDateTime(note.dateModified))
            .putString(REMINDER, DateTypeConverter().fromOffsetDateTime(note.reminder))
            .build()

        if(!cancel) {
            val notification = OneTimeWorkRequestBuilder<NotifyWorker>()
                .setInitialDelay(note.reminder?.time!! - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(note.title)
                .build()

            WorkManager.getInstance(requireContext()).enqueueUniqueWork(note.key,
                ExistingWorkPolicy.REPLACE, notification)
        }else {
            WorkManager.getInstance(requireContext()).cancelUniqueWork(note.key)
        }
    }
}
