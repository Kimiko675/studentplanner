package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Classes.Subject
import kotlinx.android.synthetic.main.item2_mark.view.*

class SubjectsMarksAdapter(private val SubjectsMarksList: MutableList<Subject>, var clickListener: OnSubjectMarkItemClickListener) : RecyclerView.Adapter<SubjectsMarksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsMarksAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item2_mark,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectsMarksAdapter.ViewHolder, position: Int) {
        val currentItem = SubjectsMarksList[position]
        holder.textView.text = currentItem.subject
        if (currentItem.mark >= 2.0)
            holder.textView2.text = currentItem.mark.toString()
        else
            holder.textView2.text = "  -"
        holder.textView3.text = currentItem.ects.toString()

        holder.initialize(SubjectsMarksList.get(position),clickListener)
    }

    override fun getItemCount(): Int {
        return SubjectsMarksList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.text_view_1Mark
        val textView2: TextView = itemView.text_view_2Mark
        val textView3: TextView = itemView.text_view_3Mark
        fun initialize(subjects: Subject, action: OnSubjectMarkItemClickListener){
            itemView.setOnClickListener{
                action.onItemClick(subjects, adapterPosition)
            }
        }
    }
}