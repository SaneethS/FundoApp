package com.yml.fundo.ui.note

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yml.fundo.R
import com.yml.fundo.common.SharedPref
import com.yml.fundo.databinding.NotePageBinding
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.ui.SharedViewModel
import com.yml.fundo.ui.wrapper.Notes
import com.yml.fundo.ui.wrapper.User
import java.util.*

class NotePage : Fragment(R.layout.note_page) {
    private lateinit var binding: NotePageBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var noteViewModel: NoteViewModel
    private var noteTitle: String? = null
    private var noteContent: String? = null
    private var noteKey: String = ""
    private var bundleDateModified: Date? = null
    private var bundleNoteId: Long? = null
    private var bundleArchived: Boolean? = null
    private var userId: Long = 0L
    private var currentUser: User =
        User(name = "Name", email = "EmailID", mobileNo = "MobileNumber")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NotePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        userId = SharedPref.getId()
        noteViewModel.getUserInfo(requireContext(), userId)

        binding.backButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
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
                    Notes(title, content, dateModified = bundleDateModified, noteKey,
                        bundleNoteId!!, archived = true)
                noteViewModel.updateNotes(requireContext(), note, currentUser)

            }else if(bundleArchived== true) {
                val title = binding.titleText.text.toString()
                val content = binding.noteText.text.toString()
                val note =
                    Notes(title, content, dateModified = bundleDateModified, noteKey,
                        bundleNoteId!!, archived = false)
                noteViewModel.updateNotes(requireContext(), note, currentUser)
            }
        }
    }

    private fun checkVisibility() {
        if(bundleNoteId == null){
            binding.deleteButton.visibility = View.GONE
            binding.archiveButton.visibility = View.GONE
        }else{
            binding.deleteButton.visibility = View.VISIBLE
            binding.archiveButton.visibility =  View.VISIBLE
        }
    }

    private fun deleteNote() {
        val title = binding.titleText.text.toString()
        val content = binding.noteText.text.toString()
        if (title.isNotEmpty() || content.isNotEmpty()) {
            val note =
                Notes(title, content, dateModified = bundleDateModified, noteKey, bundleNoteId!!)
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
        noteTitle = arguments?.getString("title")
        noteContent = arguments?.getString("notes")
        noteKey = arguments?.getString("key").toString()
        bundleDateModified = dateTime
        bundleNoteId = arguments?.getLong("id")
        bundleArchived = arguments?.getBoolean("archived")
        Log.i("BundleKey", "$bundleNoteId")
        binding.titleText.setText(noteTitle)
        binding.noteText.setText(noteContent)
    }

    private fun saveNote() {
        val title = binding.titleText.text.toString()
        val content = binding.noteText.text.toString()
        if (bundleNoteId == null) {
            val notes = Notes(title, content, dateModified = null)
            Log.i("NoteCurrent", "$currentUser")
            noteViewModel.addNewNote(requireContext(), notes, currentUser)
        } else {
            val note =
                Notes(title, content, dateModified = bundleDateModified, noteKey, bundleNoteId!!,
                bundleArchived!!)
            noteViewModel.updateNotes(requireContext(), note, currentUser)
            Toast.makeText(
                requireContext(),
                getString(R.string.updated_successfully_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}