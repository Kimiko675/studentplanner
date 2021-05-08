package com.firstapp.studentplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.detales_about_marks.*

class MarksFragment(subject: Subject): Fragment() {

    private var sub: Subject = subject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detales_about_marks,container,false)
        view.findViewById<TextView>(R.id.textviewMark).text = sub.mark.toString()
        view.findViewById<TextView>(R.id.textviewECTS).text = sub.ects.toString()
        view.findViewById<TextView>(R.id.textviewMark).setOnClickListener {
            val markPicker = MarkPicker()
            markPicker.show(requireFragmentManager(), "MarkPicker")
        }


        return view
    }

}