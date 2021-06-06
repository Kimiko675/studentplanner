package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.studentplanner.Classes.Subject
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
        //połączenie z Firebase
    auth = FirebaseAuth.getInstance();
    val userId: String = FirebaseAuth.getInstance().currentUser.uid
    val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
    val list = mutableListOf<Subject>()
    llSubjects.layoutManager = LinearLayoutManager(this)
    llSubjects.adapter = SubjectsAdapter(list,this)

        //pobieranie danych z bazy i przekazywanie ich do adaptera
    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            list.clear()
            for (i in dataSnapshot.children){
                val model= i.getValue(Subject::class.java)
                i.key?.let { Log.d("Tab", it) }
                list.add(model as Subject)
            }
            llSubjects.adapter = SubjectsAdapter(list,this@ListOfSubjects)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
        }
    }
    ref.addValueEventListener(postListener)
    }
    //uruchamianie kolejnej aktywności i przekzywanie do niej danych
    override fun onItemClick(subjects: Subject, position: Int) {
        val intent= Intent(this, DetailActivity::class.java)
        intent.putExtra("subject", subjects)
        startActivity(intent)
    }
    //uruchamianie kolejnej aktywności (edycji) i przekzywanie do niej danych
    override fun onEditClick(subjects: Subject) {
        val intent= Intent(this, EditExistingSubject::class.java)
        intent.putExtra("subject", subjects)
        startActivity(intent)
    }
    //usuwanie przedmiotu
    override fun onDeleteClick(id: String) {
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        ref.child(id).removeValue()
        Toast.makeText(this, "Przedmiot usunięty", Toast.LENGTH_SHORT).show()

    }
}