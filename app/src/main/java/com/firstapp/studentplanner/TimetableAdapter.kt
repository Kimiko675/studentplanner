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
import kotlinx.android.synthetic.main.item2.view.text_view_3
import kotlinx.android.synthetic.main.item3.view.*
import java.lang.reflect.Array.get
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class TimetableAdapter(private val SubjectsList: MutableList<Subject>) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item3,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableAdapter.ViewHolder, position: Int) {
        val currentItem = SubjectsList[position]
        holder.textView.text = currentItem.subject
        holder.textView2.text = currentItem.field
        holder.textView3.text = currentItem.hour.toString()
        holder.textView5.text = currentItem.minute.toString()
    }

    override fun getItemCount(): Int {
        return SubjectsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.text_view_1
        val textView2: TextView = itemView.text_view_2
        val textView3: TextView = itemView.text_view_3
        val textView5: TextView = itemView.text_view_5
    }
}


