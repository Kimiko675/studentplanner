package com.firstapp.studentplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.firstapp.studentplanner.Classes.Homework
import com.firstapp.studentplanner.Classes.Subject
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.dialog_add_homework.*

class AddHomework: BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth;

    private var title: String = ""
    private var description: String = ""
    private var listOfSubjects = ArrayList<String>()
    private var listOfIds = ArrayList<String>()

    // interfejs do przesyłania danych do HomeworkActivity
    lateinit var toSend: GetHomework


    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    private var hour1: Int = 0
    private var minute1: Int = 0

    private var flag1: Boolean = false
    private var flag2: Boolean = false

    lateinit var arrayAdapter: ArrayAdapter<String>

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
        val view = inflater.inflate(R.layout.dialog_add_homework,container,false)
        view.findViewById<Button>(R.id.buttonDeadlinePicker).setOnClickListener {
            val dayPicker = DeadlinePicker()
            fragmentManager?.let { it1 -> dayPicker.show(it1, "Dialog") }
            flag1 = true
            flag2 = false
        }
        view.findViewById<Button>(R.id.buttonDeadlineTimePicker).setOnClickListener {
            val timePicker = TimePicker()
            fragmentManager?.let { it1 -> timePicker.show(it1, "Dialog") }
            flag1 = false
            flag2 = true
        }

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.spinnerAddHomeworkSubjects).adapter = arrayAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        afterCheckbox.visibility = View.INVISIBLE

        // chowanie i pokazywanie suwaka do wyboru dnia przyjścia powiadomienia
        checkboxNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                afterCheckbox.visibility = View.VISIBLE
                sliderReminderDay.visibility = View.VISIBLE
            }else{
                afterCheckbox.visibility = View.INVISIBLE
                sliderReminderDay.visibility = View.INVISIBLE
            }
        }

        buttonAddHomework.setOnClickListener {
            title = editTextHomeworkTitle.text.toString()
            description = editTextHomeworkDescription.text.toString()

            if (day == 0 && month == 0 && year == 0){
                Toast.makeText(requireContext(), "Nie wybrano dnia", Toast.LENGTH_LONG).show()
            }else if (hour1 == 0 && minute1 == 0){
                Toast.makeText(requireContext(), "Nie wybrano godziny", Toast.LENGTH_LONG).show()
            }else if (title == "" && description == ""){
                Toast.makeText(requireContext(), "Nie uzupełniono wszystkich pól", Toast.LENGTH_LONG).show()
            }else{
                val subject = spinnerAddHomeworkSubjects.selectedItem.toString()
                val subjectId = listOfIds[spinnerAddHomeworkSubjects.selectedItemPosition]

                val notification = checkboxNotification.isChecked

                val reminderDay: Int
                if (notification){
                    reminderDay = sliderReminderDay.value.toInt()
                }else{
                    reminderDay = 0
                }

                val homework = Homework("",title, description, subject,subjectId, day, month, year, hour1, minute1, notification, reminderDay)

                // przesłanie danych do HomeworkActivity
                toSend.getHomework(homework)
                dismiss()
            }
        }
    }

    fun displayDay(){
        if (arguments != null && flag1){
            day = arguments?.getInt("day")!!
            month = arguments?.getInt("month")!!
            year = arguments?.getInt("year")!!

            buttonDeadlinePicker.text = day.toString() + "/" + month.toString() + "/" + year.toString()
        }
    }

    fun displayTime(){
        if (arguments != null && flag2){
            hour1 = arguments?.getInt("hour")!!
            minute1 = arguments?.getInt("minute")!!

            buttonDeadlineTimePicker.text = hour1.toString() + ":" + minute1.toString()
        }
    }

}