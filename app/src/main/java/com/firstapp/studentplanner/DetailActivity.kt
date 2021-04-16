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
        val s3:String = intent.getStringExtra("form").toString()
        editSubject.setText(s1)
        editField.setText(s2)
        editForm.setText(s3)
        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid

        btnChange.setOnClickListener{
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

}

