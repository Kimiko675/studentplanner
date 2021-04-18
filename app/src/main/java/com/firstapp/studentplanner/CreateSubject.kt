package com.firstapp.studentplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_subject.*

class CreateSubject : AppCompatActivity() {
/*
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
            val newSubject=Subject(subject, field, form)
            if (key != null) {
                ref.child(userId).child("Subjects").child(key).setValue(newSubject).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Dodano przedmiot", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Błąd", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
*/
}