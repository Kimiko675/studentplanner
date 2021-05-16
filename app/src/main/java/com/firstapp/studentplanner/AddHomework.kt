package com.firstapp.studentplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_subject.*
import kotlinx.android.synthetic.main.cyclical_subject.*
import kotlinx.android.synthetic.main.dialog_add_homework.*

class AddHomework: BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth;

    private var title: String = ""
    private var description: String = ""

    private var listOfSubjects = ArrayList<String>()
    private var listOfIds = ArrayList<String>()

    lateinit var toSend: GetHomework

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    lateinit var arrayAdapter: ArrayAdapter<String>

    //lateinit var toShow: ShowDeadlinePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);

        arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, listOfSubjects)

        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid

        val refForFields = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        val postListenerForFields = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfSubjects.clear()
                for (i in dataSnapshot.children){
                    val value = i.getValue(Subject::class.java)
                    if (value != null) {
                        listOfSubjects.add(value.subject as String)
                        listOfIds.add(value.id as String)
                    }
                }
                arrayAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refForFields.addValueEventListener(postListenerForFields)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toSend = activity as GetHomework
        //toShow = activity as ShowDeadlinePicker
        val view = inflater.inflate(R.layout.dialog_add_homework,container,false)
        view.findViewById<Button>(R.id.buttonDeadlinePicker).setOnClickListener {
            //toShow.showDeadlinePicker()
            val dayPicker = DeadlinePicker()
            fragmentManager?.let { it1 -> dayPicker.show(it1, "Dialog") }
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.spinnerAddHomeworkSubjects).adapter = arrayAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddHomework.setOnClickListener {
            title = editTextHomeworkTitle.text.toString()
            description = editTextHomeworkDescription.text.toString()

            val subject = spinnerAddHomeworkSubjects.selectedItem.toString()
            val subjectId = listOfIds[spinnerAddHomeworkSubjects.selectedItemPosition]

            val homework = Homework("",title, description, subject,subjectId, day, month, year)

            toSend.getHomework(homework)
            dismiss()
        }
    }

    public fun displayDay(){


        if (arguments != null){
            day = arguments?.getInt("day")!!
            month = arguments?.getInt("month")!!
            year = arguments?.getInt("year")!!

            buttonDeadlinePicker.text = day.toString() + "/" + month.toString() + "/" + year.toString()
        }
    }

}