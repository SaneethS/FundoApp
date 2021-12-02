package com.yml.fundo.ui.label

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yml.fundo.R
import com.yml.fundo.common.SharedPref
import com.yml.fundo.databinding.LabelCreationBinding
import com.yml.fundo.ui.SharedViewModel
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.Note
import com.yml.fundo.ui.wrapper.User
import kotlin.properties.Delegates

class LabelCreateFragment : Fragment(R.layout.label_creation) {
    private lateinit var binding: LabelCreationBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var labelCreateViewModel: LabelCreateViewModel
    private lateinit var labelCreateAdapter: LabelCreateAdapter
    private lateinit var labelRecyclerView: RecyclerView
    private var note: Note? = null
    private var userId = 0L
    private var currentUser: User = User(name = "", email = "", mobileNo = "")
    private var labelList: ArrayList<Label> = ArrayList()
    private var checkedItemList: ArrayList<Label> = ArrayList()
    private var labelNoteList: ArrayList<Label> = ArrayList()
    private var mode = 0

    companion object {
        const val ADD_MODE = 0
        const val SELECT_MODE = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LabelCreationBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        labelCreateViewModel =
            ViewModelProvider(requireActivity())[LabelCreateViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        mode = arguments?.getInt("mode")!!
        userId = SharedPref.getId()
        setLabelMode()
        initRecyclerView()
        allListeners()
        labelCreateViewModel.getUserInfo(requireContext(), userId)
        labelCreateViewModel.getLabel(requireContext())
        allObservers()
    }

    private fun setLabelMode() {
        when(mode) {
            ADD_MODE ->{}
            SELECT_MODE -> {
                binding.createLabelLayout.visibility = View.GONE
                binding.labelFab.visibility = View.VISIBLE
                note = arguments?.getSerializable("note") as Note
                labelNoteList = arguments?.getSerializable("noteLabel") as ArrayList<Label>
                Log.i("NoteLabelFragment", "labelNoteList:$labelNoteList")
            }
        }
    }

    private fun initRecyclerView() {
        labelCreateAdapter = LabelCreateAdapter(requireContext(), labelList,
            labelCreateViewModel, mode, labelNoteList)
        labelRecyclerView = binding.labelRecyclerView
        labelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.adapter = labelCreateAdapter
    }

    private fun allListeners() {
        binding.backButtonLabel.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.createLabelEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.saveLabelButton.visibility = View.VISIBLE
                binding.addLabelButton.setImageDrawable(requireActivity().getDrawable(R.drawable.close))
                binding.addLabelButton.tag = "close"
            } else {
                binding.saveLabelButton.visibility = View.GONE
                binding.addLabelButton.setImageDrawable(requireActivity().getDrawable(R.drawable.plus))
                binding.addLabelButton.tag = "add"
                binding.createLabelEditText.setText("")
            }
        }

        binding.addLabelButton.setOnClickListener {
            if (binding.addLabelButton.tag == "add") {
                binding.createLabelEditText.requestFocus()
            } else {
                binding.createLabelEditText.clearFocus()
            }
        }

        binding.saveLabelButton.setOnClickListener {
            Toast.makeText(requireContext(), "button clicked", Toast.LENGTH_SHORT).show()
            val labelName = binding.createLabelEditText.text.toString()
            val label = Label(name = labelName, dateModified = null)
            labelCreateViewModel.addNewLabel(requireContext(), label, currentUser)
        }

        binding.labelFab.setOnClickListener {
            for(item in labelList) {
                if(item.isChecked) {
                    checkedItemList.add(item)
                }
            }
            labelCreateViewModel.labelNoteLink(requireContext(), note?.key!!, checkedItemList)

            val tempLabels = ArrayList<Label>()
            tempLabels.addAll(labelNoteList)

            tempLabels.removeAll(checkedItemList)
            tempLabels.forEach {
                val linkID = "${note!!.key}_${it.fid}"
                labelCreateViewModel.removeLabelNoteLink(requireContext(), linkID)
            }
        }
    }

    private fun allObservers() {
        labelCreateViewModel.userDataStatus.observe(viewLifecycleOwner) {
            currentUser = it
        }

        labelCreateViewModel.addNewLabelStatus.observe(viewLifecycleOwner) {
            labelList.add(it)
            val pos = labelList.indexOf(it)
            labelCreateAdapter.notifyItemInserted(pos)
        }

        labelCreateViewModel.getLabelStatus.observe(viewLifecycleOwner) {
            labelList.clear()
            labelList.addAll(it)
            labelCreateAdapter.notifyDataSetChanged()
        }

        labelCreateViewModel.deleteLabelStatus.observe(viewLifecycleOwner) {
            val pos = labelList.indexOf(it)
            labelList.remove(it)
            labelCreateAdapter.notifyItemRemoved(pos)
        }

        labelCreateViewModel.updateLabelStatus.observe(viewLifecycleOwner) {
            labelList.forEachIndexed { index, label ->
                if (it.fid == label.fid) {
                    label.name = it.name
                    label.dateModified = it.dateModified
                    labelCreateAdapter.notifyItemChanged(index)
                }
            }
        }

        labelCreateViewModel.labelNoteLinkStatus.observe(viewLifecycleOwner) {
            it?.let {
                if(it) {
                    activity?.supportFragmentManager?.popBackStack()
                    labelCreateViewModel.resetLabelNoteLinkStatus()
                    Log.i("LabelBackStack", "back stack done")
                }else {
                    Toast.makeText(requireContext(), "Saving label failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}