package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.dialog_change_email.*

class Settings : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val user = FirebaseAuth.getInstance().currentUser
        val bottomSheetFragment = ChangePassword()
        val bottomSheetFragment2 = ChangeEmail()
        val bottomSheetFragment3 = DeleteAccount()
        editTextMail.text = user!!.email

        btnChangePass.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }

        btnChangeEmail.setOnClickListener {
            bottomSheetFragment2.show(supportFragmentManager,"BottomSheetDialog")
        }

        btnDeleteAccount.setOnClickListener {
            bottomSheetFragment3.show(supportFragmentManager,"BottomSheetDialog")
        }

    }
}
