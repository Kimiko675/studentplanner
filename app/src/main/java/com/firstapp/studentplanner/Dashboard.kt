package com.firstapp.studentplanner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.lang.reflect.Field

class Dashboard : AppCompatActivity(), GetPickedTime, DialogInterface.OnDismissListener {

    private lateinit var auth: FirebaseAuth;

    private val bottomSheetFragment1 = AddSubject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid



        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnAdd = findViewById<Button>(R.id.btnAddSubject)
        val btnList = findViewById<Button>(R.id.btnList)



        btnAdd.setOnClickListener{
            bottomSheetFragment1.show(supportFragmentManager,"BottomSheetDialog")
        }

        btnLogout.setOnClickListener{
            auth.signOut();
            val intent = Intent(this, Login::class.java);
            startActivity(intent);
        }

        btnList.setOnClickListener{
            val intent = Intent(this, ListOfFields::class.java);
            startActivity(intent);
        }

        btnList2.setOnClickListener{
            val intent = Intent(this, ListOfSubjects::class.java);
            startActivity(intent);
        }

        // tworzenie okna dialogowego
        val bottomSheetFragment = AddFieldOfStudy()

        btnAddField.setOnClickListener {
            // wywołanie okna dialogowego
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }

    }

    override fun getTime(hour: Int, minute: Int) {
        val bundle = Bundle()
        bundle.putInt("hour",hour)
        bundle.putInt("minute",minute)

        bottomSheetFragment1.arguments = bundle
    }

    override fun getDayStart(dayStart: Int, monthStart: Int, yearStart: Int) {
        val bundle = Bundle()
        bundle.putInt("dayStart",dayStart)
        bundle.putInt("monthStart",monthStart)
        bundle.putInt("yearStart",yearStart)
        bottomSheetFragment1.arguments = bundle
    }

    override fun getDayEnd(dayEnd: Int, monthEnd: Int, yearEnd: Int) {
        val bundle = Bundle()
        bundle.putInt("day",dayEnd)
        bundle.putInt("month",monthEnd)
        bundle.putInt("year",yearEnd)
        bottomSheetFragment1.arguments = bundle
    }

    override fun onDismiss(dialog: DialogInterface?) {
        bottomSheetFragment1.displayTime()
        bottomSheetFragment1.displayDayStart()
        bottomSheetFragment1.displayDayEnd()
        bottomSheetFragment1.displayDaySingle()
    }
}