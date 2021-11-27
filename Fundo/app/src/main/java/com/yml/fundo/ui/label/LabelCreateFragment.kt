package com.yml.fundo.ui.label

import android.os.Bundle
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
import com.yml.fundo.ui.wrapper.User

class LabelCreateFragment : Fragment(R.layout.label_creation) {
    private lateinit var binding: LabelCreationBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var labelCreateViewModel: LabelCreateViewModel
    private lateinit var labelCreateAdapter: LabelCreateAdapter
    private lateinit var labelRecyclerView: RecyclerView
    private var userId = 0L
    private var currentUser: User = User(name = "", email = "", mobileNo = "")
    private var labelList: ArrayList<Label> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LabelCreationBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        labelCreateViewModel =
            ViewModelProvider(requireActivity())[LabelCreateViewModel::class.java]
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        userId = SharedPref.getId()
        initRecyclerView()
        allListeners()
        labelCreateViewModel.getUserInfo(requireContext(), userId)
        labelCreateViewModel.getLabel(requireContext())
        allObservers()
    }

    private fun initRecyclerView() {
        labelCreateAdapter = LabelCreateAdapter(requireContext(), labelList, labelCreateViewModel)
        labelRecyclerView = binding.labelRecyclerView
        labelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.adapter = labelCreateAdapter

    }

    private fun allListeners() {
        binding.backButtonLabel.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
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
    }
}