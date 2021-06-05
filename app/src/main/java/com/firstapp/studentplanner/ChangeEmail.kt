package com.firstapp.studentplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_change_email.*
import kotlinx.android.synthetic.main.dialog_change_password.*

class ChangeEmail: BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_change_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = FirebaseAuth.getInstance().currentUser
        super.onViewCreated(view, savedInstanceState)
        editTextNewEmail.setText(user.email)
        buttonChangeEmail.setOnClickListener {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null && user.email != null) {
                        val credential: AuthCredential = EmailAuthProvider
                            .getCredential(user.email!!, editTextEmailPass.text.toString())
                        user?.reauthenticate(credential)
                            ?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    user!!.updateEmail(editTextNewEmail.text.toString())
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                user!!.sendEmailVerification()
                                                    ?.addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Log.e("Task Message", "Successful. Verfication email sent.");
                                                            Toast.makeText(context,"Email weryfikacyjny został wysłany. Wejdź w link aktywacyjny.",Toast.LENGTH_SHORT).show()
                                                            var intent = Intent(context,Login::class.java);
                                                            startActivity(intent);
                                                        }
                                                        else{
                                                            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                auth.signOut()
                                            } else {
                                                Toast.makeText(
                                                    context, "Wystąpił błąd", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, "Błędne hasło", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Błędne hasło", Toast.LENGTH_LONG)
                            .show()
                    }
        }
    }
}