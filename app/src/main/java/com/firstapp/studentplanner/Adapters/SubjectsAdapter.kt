package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Classes.Subject
import kotlinx.android.synthetic.main.item.view.text_view_1
import kotlinx.android.synthetic.main.item2.view.*

class SubjectsAdapter(private val SubjectsList: List<Subject>, var clickListener: OnSubjectItemClickListener) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectsAdapter.ViewHolder, position: Int) {
        val currentItem = SubjectsList[position]
        holder.textView.text = currentItem.subject
        holder.textView2.text = currentItem.field
        holder.imageButton.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it,SubjectsList[position])
        }
        holder.imageButton2.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it,SubjectsList[position])
        }
        holder.initialize(SubjectsList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return SubjectsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.text_view_1
        val textView2: TextView = itemView.text_view_2
        val imageButton: ImageButton = itemView.image_button_edit
        val imageButton2: ImageButton = itemView.image_button_delete
        fun initialize(subjects: Subject, action: OnSubjectItemClickListener){
            textView.text = subjects.subject
            textView2.text = subjects.field

            var forms = arrayListOf<String>("wykład","ćwiczenia","seminarium","spotkanie","zebranie","rejestracja","inne")

            itemView.setOnClickListener{
                action.onItemClick(subjects, adapterPosition)
            }

            itemView.findViewById<ImageButton>(R.id.image_button_edit).setOnClickListener {
                action.onEditClick(subjects)
            }

            itemView.findViewById<ImageButton>(R.id.image_button_delete).setOnClickListener {
                action.onDeleteClick(subjects.id.toString())
            }
        }
    }
}