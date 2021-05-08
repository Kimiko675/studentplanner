package com.firstapp.studentplanner

import android.accounts.AccountManager.get
import android.content.Intent
import android.content.res.Resources
import android.media.CamcorderProfile.get
import android.nfc.tech.Ndef.get
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.common.collect.Iterators.get
import com.google.gson.reflect.TypeToken.get
import com.google.rpc.context.AttributeContext
import io.grpc.internal.SharedResourceHolder.get
import kotlinx.android.synthetic.main.item.view.text_view_1
import kotlinx.android.synthetic.main.item2.view.*
import kotlinx.android.synthetic.main.item2.view.text_view_2
//import kotlinx.android.synthetic.main.item2.view.text_view_3
import kotlinx.android.synthetic.main.item3.view.*
import java.lang.reflect.Array.get
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class TimetableAdapter(private val SubjectsList: MutableList<ListObject>, var clickListener: OnSubItemClickListener) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item3,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableAdapter.ViewHolder, position: Int) {
        val currentItem = SubjectsList[position]
        var number: Int = position+1

        var time: String = currentItem.forms?.howLong.toString()
        var timeHH: Int = time.toInt() / 60 + currentItem.hour!!
        var timeMM: Int = time.toInt() % 60 + currentItem.minute!!
        if (timeMM >= 60) {
            timeHH += 1
            timeMM -= 60
        }
        var thisForm: String = currentItem.forms?.form.toString()

        holder.textViewNr.text = number.toString()
        holder.textView.text = currentItem.subjects!!.subject
        if (thisForm == "0") holder.textView2.text = "wykład"
        else if (thisForm == "1") holder.textView2.text = "ćwiczenia"
        else if (thisForm == "2") holder.textView2.text = "seminarium"
        else if (thisForm == "3") holder.textView2.text = "spotkanie"
        else if (thisForm == "4") holder.textView2.text = "zebranie"
        else if (thisForm == "5") holder.textView2.text = "rejestracja"
        else if (thisForm == "6") holder.textView2.text = "inne"
        holder.textView3.text = currentItem.hour.toString()
        holder.textView33.text = currentItem.minute.toString()
        if (timeHH == 0) holder.textView55.text = timeHH.toString() + "0"
        else if (timeHH >= 24){
            timeHH -= 24
            holder.textView55.text = "0" + timeHH.toString()
        }
        else holder.textView55.text = timeHH.toString()
        if (timeMM < 10) holder.textView5.text = "0" + timeMM.toString()
        else holder.textView5.text = timeMM.toString()
        holder.initialize(SubjectsList.get(position),clickListener)
    }

    override fun getItemCount(): Int {
        return SubjectsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewNr: TextView = itemView.text_view_nr
        val textView: TextView = itemView.text_view_1
        val textView2: TextView = itemView.text_view_2
        val textView3: TextView = itemView.text_view_3
        val textView33: TextView = itemView.text_view_33
        val textView55: TextView = itemView.text_view_55
        val textView5: TextView = itemView.text_view_5
        fun initialize(listObject: ListObject, action: OnSubItemClickListener){
            itemView.setOnClickListener{
                action.onItemClick(listObject, adapterPosition)
            }
        }
    }
}

interface OnSubItemClickListener{
    fun onItemClick(listObject: ListObject, position: Int)
}


