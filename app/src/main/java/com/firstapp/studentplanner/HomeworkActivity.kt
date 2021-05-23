package com.firstapp.studentplanner

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.firstapp.studentplanner"
    //private val channelId = "StudentPlanner"
    private val description = "Zadanie"

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

    @RequiresApi(Build.VERSION_CODES.O)
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

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, HomeworkDetail::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("homework", homework)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)

        notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(this, channelId)
            .setContentTitle(homework.title)
            .setContentText(homework.description)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_launcher_background))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1234,builder.build())
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