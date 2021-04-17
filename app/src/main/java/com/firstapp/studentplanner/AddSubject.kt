package com.firstapp.studentplanner

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_of_fields.*
import kotlinx.android.synthetic.main.dialog_add_field.*
import kotlinx.android.synthetic.main.dialog_add_subject.*
import kotlinx.android.synthetic.main.time_picker.*

class AddSubject: BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth;

    private var hourSetted: Int = 0
    private var minuteSetted: Int = 0

    public fun setHour(h: Int){
        hourSetted = h
    }

    public fun setMinute(m: Int){
        minuteSetted = m
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Fields")

        var list = mutableListOf<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //list.clear()
                for (i in dataSnapshot.children){
                    list.add(i.value as String)
                }

                val arrayAdapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }

                arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerFields.adapter = arrayAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        btnTimePicker.setOnClickListener() {

            val timePicker = TimePicker()

            activity?.let { it1 -> timePicker.show(it1.supportFragmentManager, "Dialog")
            }
        }

        buttonAddSubject.setOnClickListener{
            Log.d("First",hourSetted.toString())
            Log.d("First",minuteSetted.toString())

        }
    }

    public fun display(){
        hourSetted = arguments?.getInt("hour")!!
        minuteSetted = arguments?.getInt("minute")!!
        Log.d("First",hourSetted.toString())
        Log.d("First",minuteSetted.toString())

        btnTimePicker.text = hourSetted.toString() + ":" + minuteSetted.toString()
    }
}
