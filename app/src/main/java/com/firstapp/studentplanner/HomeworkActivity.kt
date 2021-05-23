package com.firstapp.studentplanner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_homework.*
import kotlinx.android.synthetic.main.detales_about_marks.*
import kotlinx.android.synthetic.main.dialog_add_homework.*

class HomeworkActivity : AppCompatActivity(), GetHomework, ConvertToAchievement, OnHomeworkItemClickListener, GetPickedDeadline, GetPickedTime, DialogInterface.OnDismissListener {

    private lateinit var auth: FirebaseAuth
    lateinit var userId: String

    private val bottomSheetFragment = AddHomework()

    private var listOfHomeworks = ArrayList<Homework>()
    var adapter = HomeworkAdapter(listOfHomeworks, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework)

        auth = FirebaseAuth.getInstance()
        userId = FirebaseAuth.getInstance().currentUser.uid

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfHomeworks.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Homework::class.java)
                    listOfHomeworks.add(model as Homework)
                }

                listOfHomeworks.sortBy { it.year }
                listOfHomeworks.sortBy { it.month }
                listOfHomeworks.sortBy { it.day }

                recyclerviewHomeworks.layoutManager = LinearLayoutManager(this@HomeworkActivity)
                recyclerviewHomeworks.adapter = adapter

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)

        buttonAddNewHomework.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
        }
    }

    override fun getHomework(homework: Homework) {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        val newRef = ref.push()
        val key = newRef.key
        val homeworkToAdd = Homework(key.toString(), homework.title,homework.description, homework.subject, homework.subjectId, homework.day, homework.month, homework.year)
        if (key != null) {
            ref.child(userId).child("Homeworks").child(key).setValue(homeworkToAdd).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Dodano pracę", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Błąd", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDeleteHomeworkClick(homework: Homework) {
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
        ref.child(homework.id).removeValue()
    }

    override fun onItemClick(homework: Homework, position: Int) {
        val intent = Intent(this, HomeworkDetail::class.java);
        intent.putExtra("homework", homework)
        startActivity(intent);
    }

    override fun onCompleteHomeworkClick(homework: Homework) {

        val bottomSheetFragment = ConvertHomeworkToAchievement(homework.title, homework.subjectId)
        bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
        ref.child(homework.id).removeValue()
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
    }

    override fun getDay(dayEnd: Int, monthEnd: Int, yearEnd: Int) {
        val bundle = Bundle()
        bundle.putInt("day",dayEnd)
        bundle.putInt("month",monthEnd)
        bundle.putInt("year",yearEnd)
        bottomSheetFragment.arguments = bundle
    }

    override fun onDismiss(dialog: DialogInterface?) {
        bottomSheetFragment.displayDay()
        bottomSheetFragment.displayTime()
    }

    override fun getTime(hour: Int, minute: Int) {
        val bundle = Bundle()
        bundle.putInt("hour",hour)
        bundle.putInt("minute",minute)
        bottomSheetFragment.arguments = bundle
    }

    override fun getDayStart(dayStart: Int, monthStart: Int, yearStart: Int) {
        TODO("Not yet implemented")
    }

    override fun getDayEnd(dayEnd: Int, monthEnd: Int, yearEnd: Int) {
        TODO("Not yet implemented")
    }


}