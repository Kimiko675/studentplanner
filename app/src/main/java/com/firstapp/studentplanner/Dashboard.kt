package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnAdd = findViewById<Button>(R.id.btnAddSubject)
        val btnList = findViewById<Button>(R.id.btnList)

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Fields")

        var list = mutableListOf<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in dataSnapshot.children){
                    list.add(i.value as String)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)

        btnAdd.setOnClickListener{
            // sprawdzenie czy w bazie jest kierunek, nie można dodać przedmiotu bez posiadania kierunku
            if (list.isNotEmpty()) {
                val intent = Intent(this,AddNewSubject::class.java);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Najpierw dodaj kierunek", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogout.setOnClickListener{
            auth.signOut();
            val intent = Intent(this, Login::class.java);
            startActivity(intent);
        }

        btnTimetable.setOnClickListener{
            val intent = Intent(this, Calendar::class.java);
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

        btnHomework.setOnClickListener {
            val intent = Intent(this, HomeworkActivity::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener{
            val intent = Intent(this, Settings::class.java);
            startActivity(intent);
        }

        // tworzenie okna dialogowego z dodawaniem kierunku
        val bottomSheetFragment = AddFieldOfStudy()

        btnAddField.setOnClickListener {
            // wywołanie okna dialogowego
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }

    }
}