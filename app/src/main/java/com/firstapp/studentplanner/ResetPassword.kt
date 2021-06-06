package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = FirebaseAuth.getInstance();

        val tvReturnToLogin = findViewById<TextView>(R.id.tvReturnToLogin)
        val btnSendLinkToResetPassword = findViewById<Button>(R.id.btnSendLinkToResetPassword)
        val editTextEmailForResetPassword = findViewById<EditText>(R.id.editEmailForResetPassword)

        // operacja wysyłania maila do resetowania hasla
        btnSendLinkToResetPassword.setOnClickListener{
            if(editTextEmailForResetPassword.text.trim().toString().isNotEmpty()) {

                Firebase.auth.sendPasswordResetEmail(editTextEmailForResetPassword.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Task message", "Email sent.")
                                Toast.makeText(this,"Wysłano link do zresetowania hasła na podany e-mail", Toast.LENGTH_LONG).show()
                            } else {
                                Log.e("Task Message", "Failes"+task.exception)
                                Toast.makeText(this,"Błąd", Toast.LENGTH_LONG).show()
                            }
                        }
            }
        }
        //powrót do panelu logowania
        tvReturnToLogin.setOnClickListener{
            val intent = Intent(this,Login::class.java);
            startActivity(intent);
        }
    }
}