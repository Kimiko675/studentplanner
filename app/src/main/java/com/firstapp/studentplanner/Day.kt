package com.firstapp.studentplanner

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import java.util.*
import java.util.Calendar


class Day: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private var c: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        var day2=c.get(Calendar.DAY_OF_WEEK) //dzien w tygodniu np.monday=2
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        val pos:String = intent.getStringExtra("position").toString()
        tvDay.text=pos
        var dni = arrayListOf<String>("poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota", "niedziela")
        var day=dni[1]

        //niedziela jest ustawiona domyslnie jako pierwszy dzien, więc to zamieniam
        if (day2==1) day2=7
        else day2-=1

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val dayS =c.get(Calendar.DAY_OF_MONTH)-(day2-1) //data pierwszego dnia w tygodniu
        val dayy = c.get(Calendar.DAY_OF_MONTH) //obecny dzien
        val daysInMonth: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH) //ilosc dni w miesiacu
        var dayE = dayS+6 //data ostatniego dnia w tygodniu
        var dayS2=dayS
        var dayE2=dayE
        if(dayE>daysInMonth) { //jesli jestesmy na przelomie miesiecy
            dayE = dayE - daysInMonth
            dayS2=1
        }


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
                        if(day==pos){
                            if(i.yearStart<year && i.yearEnd>year) {
                                val newListObject= ListObject(model, i, i.hour, i.minute)
                                list2.add(newListObject)
                            }
                            else if(i.yearStart==year && i.yearEnd==year){
                                if(i.monthStart<month && i.monthEnd>month) {
                                    val newListObject= ListObject(model, i, i.hour, i.minute)
                                    list2.add(newListObject)
                                }
                                else if(i.monthStart==month && i.monthEnd==month){
                                    if (i.dayStart<=dayy &&  i.dayEnd>=dayy){

                                            val newListObject= ListObject(model, i, i.hour, i.minute)
                                            list2.add(newListObject)
                                    }
                                }
                                /*else if(i.monthStart==month && i.monthEnd>month){
                                    if (i.dayStart<=dayy &&  i.dayEnd>=dayE){

                                        val newListObject= ListObject(model, i, i.hour, i.minute)
                                        list2.add(newListObject)
                                    }
                                }
                                else if(i.monthStart<month && i.monthEnd==month){
                                    if (i.dayStart<=dayS &&  i.dayEnd>=dayy){

                                        val newListObject= ListObject(model, i, i.hour, i.minute)
                                        list2.add(newListObject)
                                    }
                                }*/
                            }
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


