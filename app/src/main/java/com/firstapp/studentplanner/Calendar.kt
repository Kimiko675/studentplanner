package com.firstapp.studentplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.util.Calendar


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
        day_list.layoutManager = LinearLayoutManager(this)
        day_list.adapter = DayAdapter(list,this)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (j in dni) {
                    size = 0
                    for (i in dataSnapshot.children) {
                        val model = i.getValue(Subject::class.java)
                        for (i in model?.forms!!) {
                            day = dni[i.dayOfWeek]
                            if (day == j) {
                                size++
                            }
                            print(size)
                        }
                    }
                    val newListObject = DayObject(j, size)
                    list.add(newListObject)
                }
                day_list.adapter = DayAdapter(list,this@Calendar)
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
        }

        override fun onItemClick(day_object: DayObject, position: Int) {
            val intent= Intent(this, Day::class.java)
            intent.putExtra("position", day_object.day)
            startActivity(intent)

        }

}


