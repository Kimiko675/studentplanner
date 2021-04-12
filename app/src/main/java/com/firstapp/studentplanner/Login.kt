package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance();
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        tvRegister.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java);
            startActivity(intent);
        }

        btnLogin.setOnClickListener{
            if(editTextEmail.text.trim().toString().isNotEmpty() || editTextPassword.text.trim().toString().isNotEmpty()){
                signInUser(editTextEmail.text.trim().toString(),editTextPassword.text.trim().toString())
            }else{
                Toast.makeText(this,"Input required",Toast.LENGTH_LONG).show();
            }
        }

        tvForgotPassword.setOnClickListener{
            val intent = Intent(this,ResetPassword::class.java);
            startActivity(intent);
        }

    }

    fun signInUser(email:String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.e("Task Message", "Successful");
                    
                    //----------------------------
                    val user = auth.currentUser
                    if (user !=null){

                        if (user.isEmailVerified){
                            var intent = Intent(this,Dashboard::class.java);
                            startActivity(intent);
                            finish()
                        }else{
                            Toast.makeText(this,"Twój email nie został zweryfikowany.",Toast.LENGTH_LONG).show()
                        }


                    }
                    //----------------------------

                }else{
                    Log.e("Task Message", "Failes"+task.exception);
                    Toast.makeText(this,"Wrong email or password",Toast.LENGTH_LONG).show();
                }
            }
    }

}
