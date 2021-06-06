package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Classes.Achievement
import kotlinx.android.synthetic.main.item_achievement.view.*

class AchievementAdapter(private val AchievementList: ArrayList<Achievement>, var clickListener: OnAchievementItemClickListener): RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var line1: TextView = itemView.textviewTitleAchevement
        var line2: TextView = itemView.textviewDescriptionAchievement

        fun initialize(achievement: Achievement, action: OnAchievementItemClickListener){
            itemView.findViewById<ImageButton>(R.id.image_button_delete_achievement).setOnClickListener {
                action.onDeleteClick(achievement)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_achievement,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = AchievementList[position]

        holder.line1.text = currentItem.title
        holder.line2.text = currentItem.description

        holder.initialize(currentItem, clickListener)
    }

    override fun getItemCount(): Int {
        return AchievementList.size
    }

}

interface OnAchievementItemClickListener{
    fun onDeleteClick(achievement: Achievement)
}