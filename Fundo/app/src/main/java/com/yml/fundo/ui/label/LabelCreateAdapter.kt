package com.yml.fundo.ui.label

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yml.fundo.R
import com.yml.fundo.ui.wrapper.Label
import com.yml.fundo.ui.wrapper.User

class LabelCreateAdapter(
    val context: Context, private val labelList: ArrayList<Label>,
    private val labelCreateViewModel: LabelCreateViewModel
) :
    RecyclerView.Adapter<LabelCreateAdapter.LabelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.label_list,
            parent, false
        )

        return LabelViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val item = labelList[position]
        holder.label.setText(item.name)

        holder.label.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.deleteButton.setImageDrawable(context.getDrawable(R.drawable.delete_icon))
                holder.editButton.setImageDrawable(context.getDrawable(R.drawable.tick_mark))
                holder.deleteButton.tag = "delete"
                holder.editButton.tag = "tick"
            } else {
                holder.deleteButton.setImageDrawable(context.getDrawable(R.drawable.label_outlined))
                holder.editButton.setImageDrawable(context.getDrawable(R.drawable.edit_icon))
                holder.deleteButton.tag = "label"
                holder.editButton.tag = "pen"
            }
        }

        holder.deleteButton.setOnClickListener {
            if (holder.deleteButton.tag == "delete") {
                labelCreateViewModel.deleteLabel(context, item)
            } else {
                holder.label.requestFocus()
            }
        }

        holder.editButton.setOnClickListener {
            if (holder.editButton.tag == "tick") {
                if (holder.label.text.toString().isEmpty()) {
                    labelCreateViewModel.deleteLabel(context, item)
                } else if (holder.label.text.toString() != item.name) {
                    item.name = holder.label.text.toString()
                    labelCreateViewModel.updateLabel(context, item)
                }
                holder.label.clearFocus()
            } else {
                holder.label.requestFocus()
            }
        }
    }

    override fun getItemCount(): Int {
        return labelList.size
    }

    class LabelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: EditText = view.findViewById(R.id.list_label_name)
        val deleteButton: ImageView = view.findViewById(R.id.delete_label_button)
        val editButton: ImageView = view.findViewById(R.id.edit_label_button)
    }
}