package com.firstapp.studentplanner

import android.accounts.AccountManager.get
import android.content.Intent
import android.media.CamcorderProfile.get
import android.nfc.tech.Ndef.get
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.recyclerview.widget.RecyclerView
import com.google.common.collect.Iterators.get
import com.google.gson.reflect.TypeToken.get
import io.grpc.internal.SharedResourceHolder.get
import kotlinx.android.synthetic.main.item.view.text_view_1
import kotlinx.android.synthetic.main.item2.view.*
import java.lang.reflect.Array.get

class SubjectsAdapter(private val SubjectsList: MutableList<Subject>, var clickListener: OnSubjectItemClickListener) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

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
        holder.imageButton.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it,SubjectsList[position])
        }
        holder.imageButton2.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it,SubjectsList[position])
        }
        holder.initialize(SubjectsList.get(position),clickListener)
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
        fun initialize(subjects: Subject, action: OnSubjectItemClickListener){
           textView.text = subjects.subject
            textView2.text = subjects.field
            textView3.text = subjects.form

            itemView.setOnClickListener{
                action.onItemClick(subjects, adapterPosition)
            }
        }
    }
}

interface OnSubjectItemClickListener{
    fun onItemClick(subjects: Subject, position: Int)
}

