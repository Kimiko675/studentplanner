package com.firstapp.studentplanner

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.studentplanner.Classes.Achievement
import com.firstapp.studentplanner.Classes.Homework
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_homework.*
import kotlinx.android.synthetic.main.detales_about_marks.*
import kotlinx.android.synthetic.main.dialog_add_homework.*
import java.util.*
import kotlin.collections.ArrayList

class HomeworkActivity : AppCompatActivity(), GetHomework, ConvertToAchievement, OnHomeworkItemClickListener, GetPickedDeadline, GetPickedTime, DialogInterface.OnDismissListener {

    private lateinit var auth: FirebaseAuth
    lateinit var userId: String

    private val bottomSheetFragment = AddHomework()

    private var listOfHomeworks = ArrayList<Homework>()
    var adapter = HomeworkAdapter(listOfHomeworks, this)

    val channelId = "com.firstapp.studentplanner"
    private val description = "Zadanie"

    lateinit var builder: NotificationCompat.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework)

        auth = FirebaseAuth.getInstance()
        userId = FirebaseAuth.getInstance().currentUser.uid

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")


        createNotificationChannel()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfHomeworks.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Homework::class.java)
                    listOfHomeworks.add(model as Homework)
                }

                listOfHomeworks.sortBy { it.day }
                listOfHomeworks.sortBy { it.month }
                listOfHomeworks.sortBy { it.year }


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

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val title = "title"
            val description = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, title, importance)
            channel.description = description

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun hashing(string: String): Int {
        return string.hashCode()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getHomework(homework: Homework) {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        val newRef = ref.push()
        val key = newRef.key
        val homeworkToAdd = Homework(key.toString(), homework.title,homework.description, homework.subject, homework.subjectId, homework.day, homework.month, homework.year, homework.hour, homework.minute, homework.notification, homework.dayReminder)
        if (key != null) {
            ref.child(userId).child("Homeworks").child(key).setValue(homeworkToAdd).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Dodano pracę", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Błąd", Toast.LENGTH_LONG).show()
                }
            }
        }

        if (homework.notification) {





            var myCalendar = java.util.Calendar.getInstance()

            myCalendar.set(java.util.Calendar.YEAR, homework.year)
            myCalendar.set(java.util.Calendar.MONTH, homework.month-1)
            myCalendar.set(java.util.Calendar.DAY_OF_MONTH, homework.day)
            myCalendar.set(java.util.Calendar.MINUTE, homework.minute)
            myCalendar.set(java.util.Calendar.HOUR_OF_DAY, homework.hour)
            myCalendar.set(java.util.Calendar.SECOND, 0)

            myCalendar.add(java.util.Calendar.DATE, -(homework.dayReminder))

            val intent = Intent(this, NotificationBroadcast::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            /*
            val intent2 = Intent(this, HomeworkDetail::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

             */


            intent.putExtra("homeworkId", homework.id)
            intent.putExtra("homeworkTitle", homework.title)
            intent.putExtra("homeworkDescription", homework.description)
            intent.putExtra("homeworkSubject", homework.subject)
            intent.putExtra("homeworkSubjectId", homework.subjectId)
            intent.putExtra("homeworkDay", homework.day.toString())
            intent.putExtra("homeworkMonth", homework.month.toString())
            intent.putExtra("homeworkYear", homework.year.toString())
            intent.putExtra("homeworkHour", homework.hour.toString())
            intent.putExtra("homeworkMinute", homework.minute.toString())

            if (homework.notification){
                intent.putExtra("homeworkNotification", "true")
            }else{
                intent.putExtra("homeworkNotification", "false")
            }

            intent.putExtra("homeworkDayReminder", homework.dayReminder.toString())

            val pendingIntent = PendingIntent.getBroadcast(this, hashing(homework.id), intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager: AlarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)!!
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pendingIntent)

        }

    }

    /*

    fun scheduleNotification(notification: Notification, time: Long, homework: Homework, date: Date, pendingIntent: PendingIntent) {

    }

    class Receiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("HomeworkActivity", " Receiver : " + Date().toString())
        }
    }


     */

    override fun onDeleteHomeworkClick(homework: Homework) {
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Homeworks")
        ref.child(homework.id).removeValue()
    }

    override fun onItemClick(homework: Homework, position: Int) {
        val intent = Intent(this, HomeworkDetail::class.java);
        intent.putExtra("homeworkId", homework.id)
        intent.putExtra("homeworkTitle", homework.title)
        intent.putExtra("homeworkDescription", homework.description)
        intent.putExtra("homeworkSubject", homework.subject)
        intent.putExtra("homeworkSubjectId", homework.subjectId)
        intent.putExtra("homeworkDay", homework.day.toString())
        intent.putExtra("homeworkMonth", homework.month.toString())
        intent.putExtra("homeworkYear", homework.year.toString())
        intent.putExtra("homeworkHour", homework.hour.toString())
        intent.putExtra("homeworkMinute", homework.minute.toString())
        if (homework.notification){
            intent.putExtra("homeworkNotification", "true")
        }else{
            intent.putExtra("homeworkNotification", "false")
        }
        intent.putExtra("homeworkDayReminder", homework.dayReminder.toString())

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