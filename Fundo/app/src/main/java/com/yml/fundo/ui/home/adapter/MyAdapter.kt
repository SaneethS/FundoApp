package com.yml.fundo.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yml.fundo.R
import com.yml.fundo.ui.wrapper.Notes
import java.text.SimpleDateFormat

class MyAdapter(private val notesList: ArrayList<Notes>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(), Filterable {

    private lateinit var clickListener: OnItemClickListener
    private var tempNotesList = ArrayList<Notes>()

    init {
        tempNotesList = notesList
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_home,
            parent, false
        )
        return MyViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = tempNotesList[position]
        holder.title.text = item.title
        holder.content.text = item.content

        if(item.reminder != null){
            holder.reminderLayout.visibility = View.VISIBLE
            val formatter = SimpleDateFormat("dd MMM, hh:mm aa")
            val date = formatter.format(item.reminder)
            holder.reminderText.text = date
        }else {
            holder.reminderLayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return tempNotesList.size
    }

    class MyViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.list_title)
        val content: TextView = view.findViewById(R.id.list_note)
        val reminderLayout: RelativeLayout = view.findViewById(R.id.reminderLayout)
        val reminderText: TextView = view.findViewById(R.id.reminderTextView)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                tempNotesList = if (charSearch.isEmpty()) {
                    notesList
                } else {
                    val filteredList = arrayListOf<Notes>()
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
                tempNotesList = results?.values as ArrayList<Notes>
                notifyDataSetChanged()
            }
        }
    }
}