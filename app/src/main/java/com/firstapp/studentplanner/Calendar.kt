package com.firstapp.studentplanner

import android.R.attr.data
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_list_of_subjects.*
import kotlinx.android.synthetic.main.activity_login.*


class Calendar : AppCompatActivity(), OnDayItemClickListener {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var dni = arrayListOf<String>("poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota", "niedziela")
        var day = dni[1]
        var size = 0


        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        val list = mutableListOf<DayObject>()
        val list2 = mutableListOf<HourObject>()
        day_list.layoutManager = LinearLayoutManager(this)
        day_list.adapter = DayAdapter(list, this)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (j in dni) {
                    list2.clear()
                    size = 0
                    for (k in dataSnapshot.children) {
                        val model = k.getValue(Subject::class.java)
                        for (i in model?.forms!!) {
                            day = dni[i.dayOfWeek]
                            if (day == j) {
                                size++
                                val newListObject2 = HourObject(i.hour, i.minute)
                                list2.add(newListObject2)
                            }
                            else{
                                val newListObject2 = HourObject(0, 0)
                                list2.add(newListObject2)
                            }
                        }
                    }
                    list2.sortBy{it.minute}
                    list2.sortBy{it.hour}
                    var newOb =HourObject(0,0)
                    if (list2.isNotEmpty()) {
                        var newOb2 =HourObject(list2.last().hour,list2.last().minute)
                        for(k in list2){
                            if(k.hour!=0 || k.minute!=0){
                                newOb =HourObject(k.hour,k.minute)
                                break
                            }
                        }
                        val newListObject = DayObject(j, size, newOb,newOb2)
                        list.add(newListObject)
                    }

                }
                day_list.adapter = DayAdapter(list, this@Calendar)
                if (list.isEmpty()) {
                    text_freeweek.visibility = View.VISIBLE
                } else {
                    text_freeweek.visibility = View.INVISIBLE
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
        }

        override fun onItemClick(day_object: DayObject, position: Int) {
            if(day_object.size!=0){
                val intent= Intent(this, Day::class.java)
                intent.putExtra("position", day_object.day)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Brak zajęć", Toast.LENGTH_SHORT).show()
            }
        }

}


