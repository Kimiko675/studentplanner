package com.firstapp.studentplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_add_field.*

class AddFieldOfStudy: BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_field,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddField.setOnClickListener {

            val userId: String = FirebaseAuth.getInstance().currentUser.uid
            val field = editTextFieldName.text.trim().toString()

            val ref = FirebaseDatabase.getInstance().getReference("Users")
            val newRef = ref.push()
            val key = newRef.key
            if (key != null) {
                ref.child(userId).child("Fields").child(key).setValue(field).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Dodano kierunek", Toast.LENGTH_LONG).show()
                        dismiss()
                    } else {
                        Toast.makeText(context, "Błąd", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
            }
        }
    }


}