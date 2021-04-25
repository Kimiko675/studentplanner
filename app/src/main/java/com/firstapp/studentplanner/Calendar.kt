package com.firstapp.studentplanner

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list_of_subjects.*
import kotlinx.android.synthetic.main.activity_login.*


class Calendar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var dni = mutableListOf(
            "poniedziałek",
            "wtorek",
            "środa",
            "czwartek",
            "piątek",
            "sobota",
            "niedziela"
        )
        var listView = findViewById<ListView>(R.id.DayList)

        //jakis komentarz lol


        val adapter = ArrayAdapter<String>(this, R.layout.item_day, R.id.day_of_week, dni)
        listView.adapter = adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItemText = parent.getItemAtPosition(position)

                val intent= Intent(this, Day::class.java)
                intent.putExtra("position", selectedItemText.toString())
                startActivity(intent)
            }
    }

}


