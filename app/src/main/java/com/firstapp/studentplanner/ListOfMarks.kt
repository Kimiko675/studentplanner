package com.firstapp.studentplanner

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.studentplanner.Classes.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_of_marks.*
import kotlinx.android.synthetic.main.activity_list_of_subjects.*

class ListOfMarks : AppCompatActivity(), OnSubjectMarkItemClickListener {

    private lateinit var auth: FirebaseAuth;
    var fieldForMark: String = ""
    var ectsSum: Int = 0
    var markSum: Float = 0f
    var resultAverage: Float = 0f
    var emptyMark: Boolean = false
    var floatEctsSum: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_marks)

        //Przekazanie kierunku
        fieldForMark = intent.getStringExtra("field").toString()

        textView2Mark.text = fieldForMark
        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        val list = mutableListOf<Subject>()
        llSubjectsMark.layoutManager = LinearLayoutManager(this)
        llSubjectsMark.adapter = SubjectsMarksAdapter(list,this)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Subject::class.java)
                    i.key?.let { Log.d("Tab", it) }

                    if (model != null) {
                        if(model.field==fieldForMark){
                            list.add(model as Subject)

                            if (!emptyMark && model.ects != 0 && model.mark >= 2.0){
                                ectsSum += model.ects
                                floatEctsSum = model.ects.toFloat()
                                markSum += model.mark * floatEctsSum
                            }else {
                                emptyMark = true
                            }
                        }
                    }
                }
                llSubjectsMark.adapter = SubjectsMarksAdapter(list,this@ListOfMarks)

                if (!emptyMark){
                    //Wyliczenie ??redniej ocen
                    resultAverage = markSum / ectsSum
                    textView4Mark.text = String.format("%.2f",resultAverage)
                    textView4Mark.setTextColor(Color.parseColor("#FFFFFF"))
                    textView4Mark.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

    //Przej??cie do szczegu????w po klikni??ciu na przedmiot
    override fun onItemClick(subjects: Subject, position: Int) {
        val intent= Intent(this, DetailActivity::class.java)
        intent.putExtra("subject", subjects)
        startActivity(intent)
    }
}