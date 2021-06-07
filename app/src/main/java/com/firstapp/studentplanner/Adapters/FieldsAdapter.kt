package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class FieldsAdapter(private val fieldsList: MutableList<String>, var clickListener: ListOfFields) : RecyclerView.Adapter<FieldsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = fieldsList[position]
        holder.textView.text = currentItem

        holder.initialize(currentItem, clickListener)
    }

    override fun getItemCount(): Int {
        return fieldsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.image_view
        val textView: TextView = itemView.text_view_1
        fun initialize(field: String, action: OnFieldItemClickListener){
            itemView.findViewById<ImageButton>(R.id.image_button_delete_field).setOnClickListener {
                action.onDeleteClick(field)
            }
            itemView.setOnClickListener{
                action.onItemClick(field)
            }
        }
    }
}