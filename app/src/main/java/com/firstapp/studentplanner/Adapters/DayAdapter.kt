package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Classes.DayObject
//import kotlinx.android.synthetic.main.item2.view.text_view_3
import kotlinx.android.synthetic.main.item_day.view.*

class DayAdapter(private val DayList: MutableList<DayObject>, var clickListener: OnDayItemClickListener) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_day,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayAdapter.ViewHolder, position: Int) {
        val currentItem = DayList[position]
        holder.textView.text = currentItem.day.toString()
        holder.textView2.text = currentItem.size.toString()
        if(currentItem.list_object1!!.hour==0 && currentItem.list_object1!!.minute==0){
            holder.textView3.text = " "
            holder.textView4.text = " "
            holder.textView5.text = "-"
        }
        else {
            holder.textView3.text = currentItem.list_object1.hour.toString()
            if (currentItem.list_object1.minute!! < 10) {
                holder.textView4.text = "0" + currentItem.list_object1.minute.toString()
            } else {
                holder.textView4.text = currentItem.list_object1.minute.toString()
            }
        }
        if(currentItem.list_object2!!.hour==0 && currentItem.list_object2!!.minute==0){
            holder.textView6.text = " "
            holder.textView7.text = " "
            holder.textView8.text = "-"
        }
        else {
            holder.textView6.text = currentItem.list_object2.hour.toString()
            if (currentItem.list_object2.minute!! < 10) {
                holder.textView7.text = "0" + currentItem.list_object2.minute.toString()
            } else {
                holder.textView7.text = currentItem.list_object2.minute.toString()
            }
        }
        holder.initialize(DayList.get(position),clickListener)
    }

    override fun getItemCount(): Int {
        return DayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.day_of_week
        val textView2: TextView = itemView.day_size
        val textView3: TextView = itemView.hour1
        val textView4: TextView = itemView.minute1
        val textView5: TextView = itemView.kropki
        val textView6: TextView = itemView.hour2
        val textView7: TextView = itemView.minute2
        val textView8: TextView = itemView.kropki2
        fun initialize(day_object: DayObject, action: OnDayItemClickListener){
            itemView.setOnClickListener{
                action.onItemClick(day_object, adapterPosition)
            }
        }
    }
}

interface OnDayItemClickListener{
    fun onItemClick(day_object: DayObject, position: Int)
}