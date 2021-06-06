package com.firstapp.studentplanner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_change_password.*

class ChangePassword: BottomSheetDialogFragment() {

    //tworzenie wyglądu i połączenie z Firebase
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
        return inflater.inflate(R.layout.dialog_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonChangePassword.setOnClickListener {
            //sprawdzenie, czy żadne pole nie jest puste
            if (editTextOldPassword.text.isNotEmpty() && editTextNewPassword.text.isNotEmpty() && editTextNewPasswordAgain.text.isNotEmpty()) {
                //sprawdzenie, czy nowy mail został dwa razy wpisany tak samo
                if (editTextNewPassword.text.toString() == editTextNewPasswordAgain.text.toString()) {
                    val user = FirebaseAuth.getInstance().currentUser
                    //uwierzytelnianie użytkownika
                    if (user != null && user.email != null) {
                        val credential: AuthCredential = EmailAuthProvider
                            .getCredential(user.email!!, editTextOldPassword.text.toString())
                        user?.reauthenticate(credential)
                            ?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    //zmiana hasła, wylogowanie i wymuszenie ponownego zalogowania
                                    user!!.updatePassword(editTextNewPassword.text.toString())
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "Hasło zmienione",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                dismiss()
                                                auth.signOut()
                                                val intent = Intent(context, Login::class.java);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Wystąpił błąd",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Obecne hasło jest niepoprawne", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Hasła nie są takie same", Toast.LENGTH_LONG).show()
                }
                }else {
                    Toast.makeText(context, "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show()
                }
        }
    }
}
