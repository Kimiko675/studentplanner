package com.firstapp.studentplanner

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.DateFormat.Field.YEAR
import android.icu.util.Calendar.YEAR
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_homework.*
import kotlinx.android.synthetic.main.detales_about_marks.*
import kotlinx.android.synthetic.main.dialog_add_homework.*
import java.text.DateFormat.Field.YEAR
import java.util.*
import java.util.Calendar.MAY
import java.util.Calendar.YEAR
import kotlin.collections.ArrayList

class HomeworkActivity : AppCompatActivity(), GetHomework, ConvertToAchievement, OnHomeworkItemClickListener, GetPickedDeadline, GetPickedTime, DialogInterface.OnDismissListener {

    private lateinit var auth: FirebaseAuth
    lateinit var userId: String

    private val bottomSheetFragment = AddHomework()

    private var listOfHomeworks = ArrayList<Homework>()
    var adapter = HomeworkAdapter(listOfHomeworks, this)

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: NotificationCompat.Builder
    public val channelId = "com.firstapp.studentplanner"
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
        val homeworkToAdd = Homework(key.toString(), homework.title,homework.description, homework.subject, homework.subjectId, homework.day, homework.month, homework.year, homework.hour, homework.minute, homework.notification)
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
            //Notification without seted date and hour

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(this, HomeworkDetail::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra("homework", homework)
            val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(homework.title)
                .setContentText(homework.description)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(1234,builder.build())

            /*
            val myCalendaar = GregorianCalendar.getInstance()

            /*
            myCalendaar.set(GregorianCalendar.YEAR, homework.year)
            myCalendaar.set(GregorianCalendar.MONTH, homework.month)
            myCalendaar.set(GregorianCalendar.DAY_OF_MONTH, homework.day)
            myCalendaar.set(GregorianCalendar.MINUTE, homework.minute)
            myCalendaar.set(GregorianCalendar.HOUR, homework.hour)
             */

            myCalendaar.set(GregorianCalendar.YEAR, 2021)
            myCalendaar.set(GregorianCalendar.MONTH, 4)
            myCalendaar.set(GregorianCalendar.DAY_OF_MONTH, 24)
            myCalendaar.set(GregorianCalendar.MINUTE, 54)
            myCalendaar.set(GregorianCalendar.HOUR, 13)

            //myCalendaar.add(GregorianCalendar.DATE, -1)

            val date: Date = myCalendaar.time

            val intent = Intent(this, HomeworkDetail::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)

            val notificationIntent: Intent = Intent(this, MyNotification::class.java)
            notificationIntent.putExtra("notification-id", 1)
            notificationIntent.putExtra("notification", getNotification(homework, pendingIntent))
            notificationIntent.putExtra("channel-id", channelId)
            notificationIntent.putExtra("description", description)

            intent.putExtra("homework", homework)

            val alarmManager: AlarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)!!
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, date.time, pendingIntent)

            /*
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
             */
             */





        }

    }

    /*
    fun getNotification(homework: Homework, pendingIntent: PendingIntent) : Notification {
        builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(homework.title)
            .setContentText(homework.description)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_launcher_background))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setChannelId(channelId)
        return builder.build()
    }

    fun scheduleNotification(notification: Notification, time: Long, homework: Homework, date: Date, pendingIntent: PendingIntent) {

    }
     */


    class Receiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("HomeworkActivity", " Receiver : " + Date().toString())
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