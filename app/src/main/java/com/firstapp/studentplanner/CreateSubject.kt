package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_subject.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.lang.reflect.Field

class CreateSubject : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)

        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid

        btnAdd.setOnClickListener{
            val userId: String = FirebaseAuth.getInstance().currentUser.uid
            val subject = editSubject.text.toString()
            val field = editField.text.toString()
            val form = editForm.text.toString()

            val ref = FirebaseDatabase.getInstance().getReference("Users")
            val newRef = ref.push()
            val key = newRef.key
            if (key != null) {
                ref.child(userId).child("Subjects").child(key).setValue(Subject(subject,field,form)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Dodano przedmiot", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Błąd", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

}