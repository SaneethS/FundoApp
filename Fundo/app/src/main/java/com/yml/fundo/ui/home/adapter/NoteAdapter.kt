package com.yml.fundo.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.yml.fundo.R
import com.yml.fundo.ui.wrapper.Note

class NoteAdapter(private val noteList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteViewHolder>(), Filterable {

    private lateinit var clickListener: OnItemClickListener
    private var tempNotesList = ArrayList<Note>()
    private var isLoading = false

    init {
        tempNotesList = noteList
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_home,
            parent, false
        )
        return NoteViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = tempNotesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return tempNotesList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                tempNotesList = if (charSearch.isEmpty()) {
                    noteList
                } else {
                    val filteredList = arrayListOf<Note>()
                    for (i in tempNotesList) {
                        if (i.title.contains(charSearch, true) ||
                            i.content.contains(charSearch, true)
                        ) {
                            filteredList.add(i)
                        }
                    }
                    filteredList
                }
                val filteredResult = FilterResults()
                filteredResult.values = tempNotesList
                return filteredResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tempNotesList = results?.values as ArrayList<Note>
                notifyDataSetChanged()
            }
        }
    }
}