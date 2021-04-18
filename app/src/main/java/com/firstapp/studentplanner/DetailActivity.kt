package com.firstapp.studentplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_subject.*
import kotlinx.android.synthetic.main.activity_create_subject.editField
import kotlinx.android.synthetic.main.activity_create_subject.editForm
import kotlinx.android.synthetic.main.activity_create_subject.editSubject
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_list_of_subjects.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item.view.text_view_1
import kotlinx.android.synthetic.main.item2.view.*
import java.util.Collections.list

class DetailActivity : AppCompatActivity() {

        private lateinit var auth: FirebaseAuth;

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_detail)
            val s1:String = intent.getStringExtra("subject").toString()
            val s2:String = intent.getStringExtra("field").toString()
            val i11:Int = intent.getIntExtra("form",0)
            val i10:Int = intent.getIntExtra("dayWeek", 0)
            val i1: Int = intent.getIntExtra("hour",0)
            val i2: Int = intent.getIntExtra("minute",0)
            val i3: Int = intent.getIntExtra("dayStart",0)
            val i5: Int = intent.getIntExtra("monthStart",0)
            val i6: Int = intent.getIntExtra("yearStart",0)
            val i7: Int = intent.getIntExtra("dayEnd",0)
            val i8: Int = intent.getIntExtra("monthEnd",0)
            val i9: Int = intent.getIntExtra("yearEnd",0)
            val s3: String = intent.getStringExtra("time").toString()

            var forms = arrayListOf<String>("wykład","ćwiczenia","seminarium","spotkanie","zebranie","rejestracja","inne")
            var dni = arrayListOf<String>("poniedziałek","wtorek","środa","czwartek","piątek","sobota","niedziela")
            textForm.text = forms[i11]
            textDay.text = dni[i10]
            textSubject.text = s1
            textField.text = s2
            textHour.text = i1.toString()
            textMinute.text = i2.toString()
            textDayStart.text = i3.toString()
            textMonthStart.text = i5.toString()
            textYearStart.text = i6.toString()
            textDayEnd.text = i7.toString()
            textMonthEnd.text = i8.toString()
            textYearEnd.text = i9.toString()
            textTime.text = s3
        }
    }


