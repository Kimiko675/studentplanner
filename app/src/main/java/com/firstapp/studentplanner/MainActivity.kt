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

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance();
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)

        btnRegister.setOnClickListener{
            if(editEmail.text.trim().toString().isNotEmpty() || editPassword.text.trim().toString().isNotEmpty()){
                createUser(editEmail.text.trim().toString(),editPassword.text.trim().toString())
            }else{
                Toast.makeText(this,"Input Required",Toast.LENGTH_LONG).show()
            }
        }

        tvLogin.setOnClickListener{
            val intent = Intent(this,Login::class.java);
            startActivity(intent);
        }
    }
    fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){                      
                        //val user = Firebase.auth.currentUser
                        //user!!.sendEmailVerification()
                            //.addOnCompleteListener { task ->
                                //if (task.isSuccessful) {
                                    //Log.d(TAG, "Email sent.")
                                //}
                            //}                        
                        //Log.e("Task Message", "Successful");
                        //var intent = Intent(this,Dashboard::class.java);
                        //startActivity(intent);                                               
                 
                        auth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.e("Task Message", "Successful. Verfication email sent.");
                                        Toast.makeText(this,"Email weryfikacyjny został wysłany. Wejdź w link aktywacyjny.",Toast.LENGTH_LONG).show()
                                        var intent = Intent(this,Login::class.java);
                                        startActivity(intent);
                                        finish()
                                    }
                                }
                        
                    }else{
                        Log.e("Task Message", "Failes"+task.exception);
                        Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                    }
                }

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser;
        if(user!=null){
            var intent = Intent(this,Dashboard::class.java);
            startActivity(intent);
        }
    }
}
