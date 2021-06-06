package com.firstapp.studentplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firstapp.studentplanner.Classes.Achievement
import com.firstapp.studentplanner.Classes.Homework
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_homework_detail.*

class HomeworkDetail : AppCompatActivity(), ConvertToAchievement{
    private lateinit var auth: FirebaseAuth
    private lateinit var hw: Homework
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework_detail)
        //pobranie użytkownika
        userId = FirebaseAuth.getInstance().currentUser.uid

        //pobranie danych z poprzedniej aktywności
        val homeworkid = intent.getStringExtra("homeworkId").toString()
        val homeworktitle = intent.getStringExtra("homeworkTitle").toString()
        val homeworkdescription = intent.getStringExtra("homeworkDescription").toString()
        val homeworksubject = intent.getStringExtra("homeworkSubject").toString()
        val homeworksubjectid = intent.getStringExtra("homeworkSubjectId").toString()
        val homeworkday = intent.getStringExtra("homeworkDay").toString().toInt()
        val homeworkmonth = intent.getStringExtra("homeworkMonth").toString().toInt()
        val homeworkyear = intent.getStringExtra("homeworkYear").toString().toInt()
        val homeworkhour = intent.getStringExtra("homeworkHour").toString().toInt()
        val homeworkminute = intent.getStringExtra("homeworkMinute").toString().toInt()
        val string = intent.getStringExtra("homeworkNotification").toString()
        //sprawdzenie, czy użytkownik zaznaczył powiadomienia, jeśli tak, to pobieramy dodatkowe dane
        val homeworknotification: Boolean
        if (string == "true"){
            homeworknotification = true
        }else{
            homeworknotification = false
        }
        val homeworkdayreminder = intent.getStringExtra("homeworkDayReminder").toString().toInt()

        hw = Homework(homeworkid, homeworktitle, homeworkdescription, homeworksubject, homeworksubjectid, homeworkday, homeworkmonth, homeworkyear, homeworkhour, homeworkminute, homeworknotification, homeworkdayreminder)

        //wyświetlanie danych
        textView_name.text = hw.title
        textView_date.text = hw.day.toString() + "/" + hw.month.toString() + "/" + hw.year.toString() + "    " + hw.hour.toString() + ":" + if (hw.minute<10) { "0" + hw.minute.toString() } else { hw.minute.toString() }
        textView_sub.text = hw.subject
        textView_des.text = hw.description

        //usuwanie zadania
        image_button_delete_homework2.setOnClickListener{
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
            ref.child(hw.id).removeValue()
            this.finish();
        }

        //oznaczenie zadania jako skończone
        image_button_complete_homework2.setOnClickListener{
            val bottomSheetFragment = ConvertHomeworkToAchievement(hw.title, hw.subjectId)
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
            ref.child(hw.id).removeValue()
        }


    }
    //po oznaczeniu zadania jako skończone możemy zapisać je jako osiągnięcie
    override fun convertToAchievement(achievement: Achievement, subjectId: String) {
        var needToAdd: Boolean = true

        var listOfAchievement = ArrayList<Achievement>()

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects").child(subjectId).child("achievement")
        val postListenerForSubjects = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfAchievement.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Achievement::class.java)
                    listOfAchievement.add(model as Achievement)
                }
                if (needToAdd) {
                    listOfAchievement.add(achievement)
                    ref.setValue(listOfAchievement)
                    needToAdd = false
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListenerForSubjects)
        this.finish();
    }
}
