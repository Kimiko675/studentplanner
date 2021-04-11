package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_field.view.*

class FieldsAdapter(private val fieldsList: MutableList<String>) : RecyclerView.Adapter<FieldsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_field,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FieldsAdapter.ViewHolder, position: Int) {
        holder.row.text = fieldsList[position]
    }

    override fun getItemCount(): Int {
        return fieldsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val row: TextView = itemView.row
    }
}