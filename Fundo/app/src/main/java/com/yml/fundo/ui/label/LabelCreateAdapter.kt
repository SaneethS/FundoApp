package com.yml.fundo.ui.label

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yml.fundo.R
import com.yml.fundo.ui.label.LabelCreateFragment.Companion.ADD_MODE
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.User

class LabelCreateAdapter(
    val context: Context, private val labelList: ArrayList<Label>,
    private val labelCreateViewModel: LabelCreateViewModel, private val mode:Int,
    private val labelNoteList: ArrayList<Label>
) :
    RecyclerView.Adapter<LabelCreateAdapter.LabelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.label_list,
            parent, false
        )

        return LabelViewHolder(context, itemView, labelCreateViewModel, mode, labelNoteList)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val item = labelList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return labelList.size
    }

    class LabelViewHolder(val context: Context, view: View,
                          private val labelCreateViewModel: LabelCreateViewModel,
                          private val mode: Int, private val labelNoteList: ArrayList<Label>)
        : RecyclerView.ViewHolder(view) {
        private val label: EditText = view.findViewById(R.id.list_label_name)
        private val deleteButton: ImageView = view.findViewById(R.id.delete_label_button)
        private val editButton: ImageView = view.findViewById(R.id.edit_label_button)
        private val checkBox: CheckBox = view.findViewById(R.id.label_check_box)

        fun bind(item: Label) {

            if(mode == ADD_MODE) {
                label.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        deleteButton.setImageDrawable(context.getDrawable(R.drawable.delete_icon))
                        editButton.setImageDrawable(context.getDrawable(R.drawable.tick_mark))
                        deleteButton.tag = "delete"
                        editButton.tag = "tick"
                    } else {
                        deleteButton.setImageDrawable(context.getDrawable(R.drawable.label_outlined))
                        editButton.setImageDrawable(context.getDrawable(R.drawable.edit_icon))
                        deleteButton.tag = "label"
                        editButton.tag = "pen"
                    }
                }

                deleteButton.setOnClickListener {
                    if (deleteButton.tag == "delete") {
                        labelCreateViewModel.deleteLabel(context, item)
                    } else {
                        label.requestFocus()
                    }
                }

                editButton.setOnClickListener {
                    if (editButton.tag == "tick") {
                        if (label.text.toString().isEmpty()) {
                            labelCreateViewModel.deleteLabel(context, item)
                        } else if (label.text.toString() != item.name) {
                            item.name = label.text.toString()
                            labelCreateViewModel.updateLabel(context, item)
                        }
                        label.clearFocus()
                    } else {
                        label.requestFocus()
                    }
                }
            }else {
                checkBox.visibility = View.VISIBLE
                deleteButton.visibility = View.INVISIBLE
                editButton.visibility = View.INVISIBLE
                label.isFocusable = false


                checkBox.setOnCheckedChangeListener {  _, isChecked ->
                    item.isChecked = isChecked
                }

                labelNoteList.forEach {
                    if(it.fid == item.fid) {
                        checkBox.isChecked = true
                        item.isChecked = true
                    }
                }
            }

            label.setText(item.name)
        }
    }
}