package com.firstapp.studentplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Classes.Form
import kotlinx.android.synthetic.main.fragment_add_form.view.*

class FormAdapter(private val FormList: List<Form>, var clickListener: OnFormItemClickListener) : RecyclerView.Adapter<FormAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var time: Button = itemView.buttonPickTime
        var howLong: EditText = itemView.edittextHowLong
        var formOfSubject: Spinner = itemView.spinnerAddForm

        var dayOfWeek: Spinner = itemView.spinnerDayOfWeek
        var dayOfStart: Button = itemView.buttonDayStartPicker
        var dayOfEnd: Button = itemView.buttonDayEndPicker

        fun initialize(position: Int, form: Form, action: OnFormItemClickListener){
            itemView.findViewById<TextView>(R.id.textviewRemoveForm).setOnClickListener{
                action.onDeleteClick(form)
                action.updateFields()
            }
            itemView.findViewById<Button>(R.id.buttonPickTime).setOnClickListener {
                action.showTimePicker()
                action.getPosition(adapterPosition)
            }
            itemView.findViewById<Button>(R.id.buttonDayStartPicker).setOnClickListener {
                action.showDayStartPicker()
                action.getPosition(adapterPosition)
            }
            itemView.findViewById<Button>(R.id.buttonDayEndPicker).setOnClickListener {
                action.showDayEndPicker()
                action.getPosition(adapterPosition)
            }
            itemView.findViewById<Spinner>(R.id.spinnerAddForm).onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    action.getPosition(adapterPosition)
                    action.sendFormInfo(itemView.findViewById<Spinner>(R.id.spinnerAddForm).selectedItemPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            itemView.findViewById<Spinner>(R.id.spinnerDayOfWeek).onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    action.getPosition(adapterPosition)
                    action.sendDayOfWeekInfo(itemView.findViewById<Spinner>(R.id.spinnerDayOfWeek).selectedItemPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            itemView.findViewById<EditText>(R.id.edittextHowLong).addTextChangedListener {
                action.getPosition(adapterPosition)
                action.sendHowLong(itemView.findViewById<EditText>(R.id.edittextHowLong).text.toString())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.fragment_add_form,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormAdapter.ViewHolder, position: Int) {
        holder.setIsRecyclable(false);

        var currentItem = FormList[position]

        if (currentItem.form != -1){
            holder.formOfSubject.setSelection(currentItem.form)
        }

        if (currentItem.hour != -1){
            holder.time.text = currentItem.hour.toString() + ":" + currentItem.minute.toString()
        }

        if (currentItem.howLong != ""){
            holder.howLong.setText(currentItem.howLong)
        }

        if (currentItem.dayOfWeek != -1){
            holder.dayOfWeek.setSelection(currentItem.dayOfWeek)
        }

        if (currentItem.dayStart != -1){
            holder.dayOfStart.text = currentItem.dayStart.toString() + "/" + currentItem.monthStart.toString() + "/" + currentItem.yearStart.toString()
        }

        if (currentItem.dayEnd != -1){
            holder.dayOfEnd.text = currentItem.dayEnd.toString() + "/" + currentItem.monthEnd.toString() + "/" + currentItem.yearEnd.toString()
        }

        holder.initialize(position, currentItem, clickListener)
    }

    override fun getItemCount(): Int {
        return FormList.size
    }
}