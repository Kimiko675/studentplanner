package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance();

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnAdd = findViewById<Button>(R.id.btnAddSubject)

        btnAdd.setOnClickListener{
            val intent = Intent(this,CreateSubject::class.java);
            startActivity(intent);
        }

        btnLogout.setOnClickListener{
            auth.signOut();
            val intent = Intent(this, Login::class.java);
            startActivity(intent);
        }

        // tworzenie okna dialogowego
        val bottomSheetFragment = AddFieldOfStudy()

        btnAddField.setOnClickListener {
            // wywołanie okna dialogowego
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }
    }
}