package com.firstapp.studentplanner

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

        btnAdd.setOnClickListener{
            val intent = Intent(this,CreateSubject::class.java);
            startActivity(intent);
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

        // tworzenie okna dialogowego
        val bottomSheetFragment = AddFieldOfStudy()

        btnAddField.setOnClickListener {
            // wywołanie okna dialogowego
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }

    }
}