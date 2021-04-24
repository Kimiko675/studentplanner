package com.firstapp.studentplanner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_list_of_subjects.*
import kotlinx.android.synthetic.main.dialog_add_subject.*

class Day: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        val pos:String = intent.getStringExtra("position").toString()
        tvDay.text=pos
        var dni = arrayListOf<String>("poniedziałek","wtorek","środa","czwartek","piątek","sobota","niedziela")


        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
            .orderByChild("hour")
        val list = mutableListOf<Subject>()
        llTimetable.layoutManager = LinearLayoutManager(this)
        llTimetable.adapter = TimetableAdapter(list)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Subject::class.java)
                    val day = dni[model?.dayOfWeek!!]
                    if (day==pos) {
                        i.key?.let { Log.d("Tab", it) }
                        list.add(model as Subject)
                    }
                }
                llTimetable.adapter = TimetableAdapter(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

    }

