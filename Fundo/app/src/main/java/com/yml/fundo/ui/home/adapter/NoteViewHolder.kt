package com.yml.fundo.ui.home.adapter

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yml.fundo.R
import com.yml.fundo.ui.wrapper.Note
import java.text.SimpleDateFormat

class NoteViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.list_title)
    private val content: TextView = view.findViewById(R.id.list_note)
    private val reminderLayout: RelativeLayout = view.findViewById(R.id.reminderLayout)
    private val reminderText: TextView = view.findViewById(R.id.reminderTextView)

    fun bind(item: Note) {
        title.text = item.title

        if (item.content.isEmpty()) {
            content.isVisible = false
        } else {
            content.isVisible = true
            content.text = item.content
        }

        if (item.reminder != null) {
            reminderLayout.visibility = View.VISIBLE
            val formatter = SimpleDateFormat("dd MMM, hh:mm aa")
            val date = formatter.format(item.reminder!!)
            reminderText.text = date
        } else {
            reminderLayout.visibility = View.GONE
        }
    }

    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}