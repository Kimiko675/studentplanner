package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_homework_detail.*
import kotlinx.android.synthetic.main.detales_about_subject.*

class HomeworkDetail : AppCompatActivity(), ConvertToAchievement{
    private lateinit var auth: FirebaseAuth
    private lateinit var hw: Homework
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework_detail)
        userId = FirebaseAuth.getInstance().currentUser.uid

        hw = intent.getSerializableExtra("homework") as Homework;

        textView_name.text = hw.title
        textView_date.text = hw.day.toString() + "/" + hw.month.toString() + "/" + hw.year.toString()
        textView_sub.text = hw.subject
        textView_des.text = hw.description

        image_button_delete_homework2.setOnClickListener{
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
            ref.child(hw.id).removeValue()
            this.finish();
        }

        image_button_complete_homework2.setOnClickListener{
            val bottomSheetFragment = ConvertHomeworkToAchievement(hw.title, hw.subjectId)
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
            ref.child(hw.id).removeValue()
        }


    }

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
