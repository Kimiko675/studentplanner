package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_of_subjects.*

class ListOfSubjects : AppCompatActivity(), OnSubjectItemClickListener {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_subjects)

    auth = FirebaseAuth.getInstance();
    val userId: String = FirebaseAuth.getInstance().currentUser.uid

    val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")

    val list = mutableListOf<Subject>()
    llSubjects.layoutManager = LinearLayoutManager(this)
    llSubjects.adapter = SubjectsAdapter(list,this)

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            list.clear()
            for (i in dataSnapshot.children){
                val model= i.getValue(Subject::class.java)
                list.add(model as Subject)
            }
            llSubjects.adapter = SubjectsAdapter(list,this@ListOfSubjects)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
        }

    }
    ref.addValueEventListener(postListener)
    }



    override fun onItemClick(subjects: Subject, position: Int) {
        /*
        val intent= Intent(this, DetailActivity::class.java)
        intent.putExtra("subject", subjects.subject)
        intent.putExtra("field", subjects.field)
        intent.putExtra("form", subjects.form)
        startActivity(intent)

         */
    }
}