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
    private var listOfForms = ArrayList<Form>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        val pos:String = intent.getStringExtra("position").toString()
        tvDay.text=pos
        var dni = arrayListOf<String>("poniedziałek","wtorek","środa","czwartek","piątek","sobota","niedziela")
        var day=dni[1]
        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        val list = mutableListOf<Subject>()
        val list2 = mutableListOf<ListObject>()
        llTimetable.layoutManager = LinearLayoutManager(this)
        llTimetable.adapter = TimetableAdapter(list2)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Subject::class.java)
                    for (i in model?.forms!!){
                        day = dni[i.dayOfWeek]
                        if (day==pos) {
                            val newListObject= ListObject(model, i,i.hour,i.minute)
                            list2.add(newListObject)
                        }
                    }

                }
                list2.sortBy{it.minute}
                list2.sortBy{it.hour}
                llTimetable.adapter = TimetableAdapter(list2)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

    }


