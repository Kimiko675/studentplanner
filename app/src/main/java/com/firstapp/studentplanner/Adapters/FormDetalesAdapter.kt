package com.firstapp.studentplanner.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Classes.Form
import com.firstapp.studentplanner.R
import kotlinx.android.synthetic.main.detales_about_form.view.*

class FormDetalesAdapter(private val FormList: List<Form>): RecyclerView.Adapter<FormDetalesAdapter.ViewHolder>() {

    private val forms = mutableListOf("wykład", "ćwiczenia", "seminarium", "spotkanie", "zebranie", "rejestracja", "inne")

    private val days = mutableListOf("poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota", "niedziela")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time: TextView = itemView.textviewTime
        var howLong: TextView = itemView.textviewHowLong
        var formOfSubject: TextView = itemView.textviewForm

        var dayOfWeek: TextView = itemView.textviewDayOfWeek
        var dayOfStart: TextView = itemView.textviewDayStar
        var dayOfEnd: TextView = itemView.textviewDayEnd

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.detales_about_form,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = FormList[position]

        holder.time.text = currentItem.hour.toString() + ":" + currentItem.minute.toString()

        holder.howLong.text = currentItem.howLong

        holder.formOfSubject.text = forms[currentItem.form]
        holder.dayOfWeek.text = days[currentItem.dayOfWeek]
        holder.dayOfStart.text = currentItem.dayStart.toString() + "/" + currentItem.monthStart.toString() + "/" + currentItem.yearStart.toString()
        holder.dayOfEnd.text = currentItem.dayEnd.toString() + "/" + currentItem.monthEnd.toString() + "/" + currentItem.yearEnd.toString()

    }

    override fun getItemCount(): Int {
        return FormList.size
    }
}