package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = FirebaseAuth.getInstance();

        val tvReturnToLogin = findViewById<TextView>(R.id.tvReturnToLogin)
        val btnSendLinkToResetPassword = findViewById<Button>(R.id.btnSendLinkToResetPassword)
        val editTextEmailForResetPassword = findViewById<EditText>(R.id.editEmailForResetPassword)

        btnSendLinkToResetPassword.setOnClickListener{
            if(editTextEmailForResetPassword.text.trim().toString().isNotEmpty()) {
                // dodać operację wysyłania maila
                Toast.makeText(this,"Wysłano link do zresetowania hasła na podany e-mail", Toast.LENGTH_LONG).show();
            }
        }

        tvReturnToLogin.setOnClickListener{
            val intent = Intent(this,Login::class.java);
            startActivity(intent);
        }



    }
}