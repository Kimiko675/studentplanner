package com.firstapp.studentplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.studentplanner.Classes.ListObject
import com.firstapp.studentplanner.Classes.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_list_of_subjects.*
import java.util.*
import java.util.Calendar


class Day: AppCompatActivity(), OnSubItemClickListener {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        val pos:String = intent.getStringExtra("position").toString()
        tvDay.text=pos
        var dni = arrayListOf<String>("poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota", "niedziela")
        var day=dni[1]

        //połącznie z Firebase
        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        val list = mutableListOf<Subject>()
        val list2 = mutableListOf<ListObject>()

        //przypisanie adaptera do listy
        llTimetable.layoutManager = LinearLayoutManager(this)
        llTimetable.adapter = TimetableAdapter(list2,this)
        //wyszkuwanie przedmiotów odbywająych się w odpowiedni dzień i zapisywanie ich do listy
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Subject::class.java)
                    for (i in model?.forms!!){
                        day = dni[i.dayOfWeek]
                        if(day==pos){
                                val newListObject= ListObject(model, i, i.hour, i.minute)
                                list2.add(newListObject)
                        }
                    }

                }
                //sortowanie listy według godzin i minut
                list2.sortBy{it.minute}
                list2.sortBy{it.hour}
                llTimetable.adapter = TimetableAdapter(list2,this@Day)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

    //uruchamnianie nowej aktywności i przekazywanie do niej danych
    override fun onItemClick(listObject: ListObject, position: Int) {
        val intent= Intent(this, DetailActivity::class.java)
        intent.putExtra("subject", listObject.subjects)
        startActivity(intent)
    }
}



