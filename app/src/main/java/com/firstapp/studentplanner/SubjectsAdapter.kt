package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item.view.text_view_1
import kotlinx.android.synthetic.main.item2.view.*

class SubjectsAdapter(private val SubjectsList: MutableList<Subject>) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectsAdapter.ViewHolder, position: Int) {
        val currentItem = SubjectsList[position]
        holder.textView.text = currentItem.subject
        holder.textView2.text = currentItem.field
        holder.textView3.text = currentItem.form
        holder.imageButton.image_button_edit.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it,SubjectsList[position])}
        holder.imageButton2.image_button_delete.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it,SubjectsList[position])}
    }

    override fun getItemCount(): Int {
        return SubjectsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.text_view_1
        val textView2: TextView = itemView.text_view_2
        val textView3: TextView = itemView.text_view_3
        val imageButton: ImageButton = itemView.image_button_edit
        val imageButton2: ImageButton = itemView.image_button_delete
    }
}