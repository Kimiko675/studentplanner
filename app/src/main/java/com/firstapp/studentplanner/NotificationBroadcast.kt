package com.firstapp.studentplanner

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.common.hash.Hashing
import kotlin.concurrent.timer

class NotificationBroadcast: BroadcastReceiver() {

    lateinit var notifyid: String
    lateinit var notifytitle: String
    lateinit var notifydescription: String
    lateinit var notifysubject: String
    lateinit var notifysubjectid: String
    var notifyday: Int = 0
    var notifymonth: Int = 0
    var notifyyear: Int = 0
    var notifyhour: Int = 0
    var notifyminute: Int = 0
    var notifynotification: Boolean = false
    var notifydayreminder: Int = 0

    fun hashing(string: String): Int {
        return string.hashCode()
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }

        if (intent != null) {

            notifyid = intent.getStringExtra("homeworkId").toString()
            notifytitle = intent.getStringExtra("homeworkTitle").toString()
            notifydescription = intent.getStringExtra("homeworkDescription").toString()
            notifysubject = intent.getStringExtra("homeworkSubject").toString()
            notifysubjectid = intent.getStringExtra("homeworkSubjectId").toString()
            notifyday = intent.getStringExtra("homeworkDay").toString().toInt()
            notifymonth = intent.getStringExtra("homeworkMonth").toString().toInt()
            notifyyear = intent.getStringExtra("homeworkYear").toString().toInt()
            notifyhour = intent.getStringExtra("homeworkHour").toString().toInt()
            notifyminute = intent.getStringExtra("homeworkMinute").toString().toInt()
            val string = intent.getStringExtra("homeworkNotification").toString()
            if (string == "true"){
                notifynotification = true
            }else{
                notifynotification = false
            }
            notifydayreminder = intent.getStringExtra("homeworkDayReminder").toString().toInt()
        }


        val intent2 = Intent(context, HomeworkDetail::class.java)

        intent2.putExtra("homeworkId", notifyid)
        intent2.putExtra("homeworkTitle", notifytitle)
        intent2.putExtra("homeworkDescription", notifydescription)
        intent2.putExtra("homeworkSubject", notifysubject)
        intent2.putExtra("homeworkSubjectId", notifysubjectid)
        intent2.putExtra("homeworkDay", notifyday.toString())
        intent2.putExtra("homeworkMonth", notifymonth.toString())
        intent2.putExtra("homeworkYear", notifyyear.toString())
        intent2.putExtra("homeworkHour", notifyhour.toString())
        intent2.putExtra("homeworkMinute", notifyminute.toString())

        if (notifynotification){
            intent2.putExtra("homeworkNotification", "true")
        }else{
            intent2.putExtra("homeworkNotification", "false")
        }

        intent2.putExtra("homeworkDayReminder", notifydayreminder.toString())

        val pendingIntent = PendingIntent.getActivity(context, 0,
            intent2, 0)


        val builder = context?.let {
            NotificationCompat.Builder(it, "com.firstapp.studentplanner")
                .setContentTitle(notifytitle)
                .setContentText("Zbliża się termin!   " + notifyday.toString() + "/" + notifymonth.toString() + "/" + notifyyear.toString() + " - " + if (notifyhour<10) { "0" + notifyhour.toString()} else {notifyhour.toString()} + ":" + if (notifyminute<10) { "0" + notifyminute.toString()} else {notifyminute.toString()})
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }



        if (builder != null) {
            if (notificationManager != null) {
                notificationManager.notify(hashing(notifyid), builder.build())
            }
        }
    }


}