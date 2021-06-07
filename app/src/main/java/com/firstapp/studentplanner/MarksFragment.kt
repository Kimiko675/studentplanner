package com.firstapp.studentplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.firstapp.studentplanner.Classes.Subject

class MarksFragment(subject: Subject, userID: String): Fragment() {

    private var sub: Subject = subject

    private lateinit var action: SetRecyclerViewMark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action = activity as SetRecyclerViewMark
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detales_about_marks,container,false)

        if (sub.mark == 0f){
            view.findViewById<TextView>(R.id.textviewMark).text = "-"
        }else{
            view.findViewById<TextView>(R.id.textviewMark).text = sub.mark.toString()
        }
        view.findViewById<TextView>(R.id.textviewECTS).text = sub.ects.toString()

        action.setAchievements()


        view.findViewById<TextView>(R.id.textviewMark).setOnClickListener {
            val markPicker = MarkPicker()
            markPicker.show(requireFragmentManager(), "MarkPicker")
        }

        val bottomSheetFragment = AddAchievement()

        view.findViewById<Button>(R.id.buttonAddNewAchievement).setOnClickListener {
            bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")
        }
        return view
    }
}

//Służy do przesłania danych do DetailActivity
interface SetRecyclerViewMark{
    fun setAchievements()
}