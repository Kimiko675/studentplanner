package com.firstapp.studentplanner

import android.view.View
import com.firstapp.studentplanner.Classes.Subject

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, subject: Subject){

    }
}