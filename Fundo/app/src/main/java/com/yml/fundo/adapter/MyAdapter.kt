package com.yml.fundo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.yml.fundo.R
import com.yml.fundo.model.Notes

class MyAdapter(private val notesList: ArrayList<Notes>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_home, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = notesList[position]
        holder.title.text = item.title
        holder.content.text = item.content
    }

    override fun getItemCount(): Int {
        return  notesList.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.list_title)
        val content: TextView = view.findViewById(R.id.list_note)
    }
}