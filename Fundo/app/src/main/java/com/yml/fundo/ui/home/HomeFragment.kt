package com.yml.fundo.ui.home

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.button.MaterialButton
import com.yml.fundo.R
import com.yml.fundo.common.ARCHIVE
import com.yml.fundo.common.REMINDER
import com.yml.fundo.ui.home.adapter.MyAdapter
import com.yml.fundo.databinding.HomePageBinding
import com.yml.fundo.common.SharedPref
import com.yml.fundo.common.TYPE
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.ui.SharedViewModel
import com.yml.fundo.ui.wrapper.Notes
import com.yml.fundo.ui.wrapper.User
import com.yml.fundo.ui.note.NoteFragment

class HomeFragment
    : Fragment(R.layout.home_page) {
    private lateinit var binding: HomePageBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var loading: Dialog
    private lateinit var dialogView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var type: String
    private var isLoading: Boolean = false
    private var totalNotes: Int = 0
    private var menu: Menu? = null
    private var userId: Long = 0L
    private var notesList = ArrayList<Notes>()
    private var currentUser: User =
        User(name = "Name", email = "EmailID", mobileNo = "MobileNumber")


    companion object {
        private const val STORAGE_PERMISSION_RESULTCODE = 0
        private const val PICK_IMAGE_RESULTCODE = 1
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomePageBinding.bind(view)
        sharedViewModel = ViewModelProvider(
            requireActivity()
        )[SharedViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        loading = Dialog(requireContext())
        loading.setContentView(R.layout.loading_screen)

        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)
        type = arguments?.getString(TYPE).toString()

        userId = SharedPref.getId()

        Log.i("Home", "$userId")
        homeViewModel.getUserInfo(requireContext(), userId)
        profilePage()
        userData()
        setUserDetails()
        notesCount()


        binding.homeFab.setOnClickListener {
            sharedViewModel.setGoToNotePageStatus(true)
        }

        refreshNotes()

        homeViewModel.userAvatarStatus.observe(viewLifecycleOwner) {
            val userProfileIcon: ImageButton = dialogView.findViewById(R.id.dialog_profile_icon)
            userProfileIcon.setImageBitmap(it)

            val item = menu?.findItem(R.id.profile_icon)
            item?.icon = BitmapDrawable(it)

            loading.dismiss()
        }

        myRecyclerView()
        noteClick()
        checkVisibility()
    }

    private fun notesCount() {
        when (type) {
            ARCHIVE -> {
                homeViewModel.getArchiveCount(requireContext())
            }
            REMINDER -> {
                homeViewModel.getReminderCount(requireContext())
            }
            else -> {
                homeViewModel.getNotesCount(requireContext())
            }
        }

    }

    private fun checkVisibility() {
        if (type == ARCHIVE || type == REMINDER) {
            binding.homeFab.visibility = View.GONE
        }
    }

    private fun refreshNotes() {
        binding.swipeRefreshView.setOnRefreshListener {
            homeViewModel.syncData(requireContext(), currentUser)
        }

        homeViewModel.syncDataStatus.observe(viewLifecycleOwner) {
            homeViewModel.getNewNotes(requireContext())
            binding.swipeRefreshView.isRefreshing = false
        }
    }

    private fun userData() {
        homeViewModel.userDataStatus.observe(viewLifecycleOwner) {
            currentUser = it
            setUserDetails()
        }
    }

    private fun setUserDetails() {
        val name: TextView = dialogView.findViewById(R.id.dialog_name)
        val email: TextView = dialogView.findViewById(R.id.dialog_email)

        name.text = currentUser.name
        email.text = currentUser.email
    }

    private fun noteClick() {
        myAdapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val note = notesList[position]
                val bundle = Bundle()
                val dateTime = DateTypeConverter().fromOffsetDateTime(note.dateModified)
                bundle.putString("title", note.title)
                bundle.putString("notes", note.content)
                bundle.putString("key", note.key)
                bundle.putLong("id", note.id)
                bundle.putString("dateModified", dateTime)
                bundle.putBoolean("archived", note.archived)
                bundle.putString("reminder", DateTypeConverter().fromOffsetDateTime(note.reminder))
                val notePage = NoteFragment()
                notePage.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_view, notePage).commit()
            }
        })
    }

    private fun myRecyclerView() {
        myAdapter = MyAdapter(notesList)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.setHasFixedSize(true)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                    val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val firstItemPositionArray = layoutManager.findFirstVisibleItemPositions(null)
                    val firstItemPosition = firstItemPositionArray[0]

                    Log.i("recyclerScroll", "$totalNotes")
                    if (((visibleItemCount + firstItemPosition) >= totalItem) && (totalItem < totalNotes)) {
                        isLoading = true
                        notesLoader()
                        when (type) {
                            ARCHIVE -> {
                                homeViewModel.getArchivePaged(requireContext(), 10, totalItem)
                            }
                            REMINDER -> {
                                homeViewModel.getReminderPaged(requireContext(), 10, totalItem)
                            }
                            else -> {
                                homeViewModel.getPagedNotes(requireContext(), 10, totalItem)
                            }
                        }
                    }
                } else {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val firstItemPosition = layoutManager.findFirstVisibleItemPosition()

                    Log.i("recyclerScroll", "$totalNotes")
                    if (((visibleItemCount + firstItemPosition) >= totalItem) && (totalItem < totalNotes)) {
                        isLoading = true
                        notesLoader()
                        when (type) {
                            ARCHIVE -> {
                                homeViewModel.getArchivePaged(requireContext(), 10, totalItem)
                            }
                            REMINDER -> {
                                homeViewModel.getReminderPaged(requireContext(), 10, totalItem)
                            }
                            else -> {
                                homeViewModel.getPagedNotes(requireContext(), 10, totalItem)
                            }
                        }
                    }
                }
            }
        })

        recyclerView.adapter = myAdapter

        when (type) {
            ARCHIVE -> {
                homeViewModel.getArchivedNotes(requireContext())
            }
            REMINDER -> {
                homeViewModel.getReminderNotes(requireContext())
            }
            else -> {
                homeViewModel.getNewNotes(requireContext())
            }
        }

        notesObserver()
        archivedObserver()
        reminderObserver()
    }

    private fun notesObserver() {
        homeViewModel.getNewNotesStatus.observe(viewLifecycleOwner) {
            notesList.clear()
            notesList.addAll(it)
            homeViewModel.getNotesCount(requireContext())
            myAdapter.notifyDataSetChanged()
        }

        homeViewModel.getPagedNotesStatus.observe(viewLifecycleOwner) {
            isLoading = false
            notesList.addAll(it)
            myAdapter.notifyDataSetChanged()
            notesLoader()
        }

        homeViewModel.getNoteCount.observe(viewLifecycleOwner) {
            totalNotes = it
        }
    }

    private fun archivedObserver() {
        homeViewModel.getArchiveNotesStatus.observe(viewLifecycleOwner) {
            notesList.clear()
            notesList.addAll(it)
            homeViewModel.getArchiveCount(requireContext())
            myAdapter.notifyDataSetChanged()
        }

        homeViewModel.getArchiveCount.observe(viewLifecycleOwner) {
            totalNotes = it
        }

        homeViewModel.getArchivePagedStatus.observe(viewLifecycleOwner) {
            isLoading = false
            notesList.addAll(it)
            myAdapter.notifyDataSetChanged()
            notesLoader()
        }
    }

    private fun reminderObserver() {
        homeViewModel.getReminderNotesStatus.observe(viewLifecycleOwner) {
            notesList.clear()
            notesList.addAll(it)
            homeViewModel.getReminderCount(requireContext())
            myAdapter.notifyDataSetChanged()
        }

        homeViewModel.getReminderCount.observe(viewLifecycleOwner) {
            totalNotes = it
        }

        homeViewModel.getReminderPagedStatus.observe(viewLifecycleOwner) {
            isLoading = false
            notesList.addAll(it)
            myAdapter.notifyDataSetChanged()
            notesLoader()
        }
    }

    private fun notesLoader() {
        if (isLoading) {
            binding.recyclerProgressBar.visibility = View.VISIBLE
        } else {
            binding.recyclerProgressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_RESULTCODE && data != null) {
            loading.show()
            var imageUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            homeViewModel.setUserAvatar(bitmap)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_RESULTCODE && grantResults.isNotEmpty()) {
            if ((grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_access_required_toast),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        changeNotesLayout()
        searchNotes()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.profile_icon -> showDialog()
        }
        return false
    }

    private fun changeNotesLayout() {
        val toggleView = menu?.getItem(1)
        val view = toggleView?.actionView
        val viewSelector = view?.findViewById<AppCompatToggleButton>(R.id.toggle_button)
        viewSelector?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            } else {
                recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
            }

        }
    }

    private fun searchNotes() {
        val searchItem = menu?.getItem(0)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter.filter.filter(newText)
                return false
            }

        })
    }

    private fun showDialog() {
        alertDialog.show()
    }

    private fun dismissDialog() {
        alertDialog.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun profilePage() {
        dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.profile_dialog, null)
        alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        homeViewModel.getUserAvatar()
        val logout: MaterialButton = dialogView.findViewById(R.id.dialog_logout)
        logout.setOnClickListener {
            homeViewModel.logoutFromHome(requireContext())
            dismissDialog()
            sharedViewModel.setGoToLoginPageStatus(true)
        }

        val close: ImageView = dialogView.findViewById(R.id.close_icon)
        close.setOnClickListener {
            dismissDialog()
        }


        val profileIcon: ImageButton = dialogView.findViewById(R.id.dialog_profile_icon)
        profileIcon.setOnClickListener {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_RESULTCODE
                )
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_RESULTCODE)
    }
}
