package com.firstapp.studentplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bottomSheetFragment = ChangePassword()

        btnChangePass.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }

    }
}