package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_homework.view.*

class HomeworkAdapter(private val HomeworksList: MutableList<Homework>, var clickListener: OnHomeworkItemClickListener) : RecyclerView.Adapter<HomeworkAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_homework,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeworkAdapter.ViewHolder, position: Int) {
        val currentItem = HomeworksList[position]
        holder.textView.text = currentItem.title
        holder.textView4.text = currentItem.day.toString() + "/" + currentItem.month.toString() + "/" + currentItem.year.toString()

        //day.toString() + "/" + month.toString() + "/" + year.toString()

        holder.initialize(HomeworksList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return HomeworksList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.textviewTitleHomework
        val textView4: TextView = itemView.textviewDeadline
        fun initialize(homework: Homework, action: OnHomeworkItemClickListener){

            itemView.findViewById<ImageButton>(R.id.image_button_delete_homework).setOnClickListener {
                action.onDeleteHomeworkClick(homework)
            }

            itemView.findViewById<ImageButton>(R.id.image_button_complete_homework).setOnClickListener {
                action.onCompleteHomeworkClick(homework)
            }

            itemView.setOnClickListener{
                action.onItemClick(homework, adapterPosition)
            }
        }
    }
}

interface OnHomeworkItemClickListener {
    fun onDeleteHomeworkClick(homework: Homework)
    fun onCompleteHomeworkClick(homework: Homework)
    fun onItemClick(homework: Homework, position: Int)
}
