package com.firstapp.studentplanner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_subject.*

class EditExistingSubject: AppCompatActivity(), OnFormItemClickListener,GetPickedTime, DialogInterface.OnDismissListener {

    private lateinit var auth: FirebaseAuth;

    private var listOfForms = ArrayList<Form>()
    private var myAdapter = FormAdapter(listOfForms, this@EditExistingSubject)

    private var listOfFields = ArrayList<String>()

    private var positionOfCurrentForm: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject)

        val sub: Subject = intent.getSerializableExtra("subject") as Subject;

        tvTitleAddSubject.text = "Edycja przedmiotu"
        buttonAddSubject.text = "Zedytuj przedmiot"


        edittextAddSubjectName.setText(sub.subject)
        edittextECTS.setText(sub.ects.toString().toInt())


        for (i in sub.forms!!){
            listOfForms.add(i)
        }
        myAdapter.notifyDataSetChanged()

        val arrayAdapter = ArrayAdapter<String>(this@EditExistingSubject, android.R.layout.simple_spinner_item, listOfFields)

        // wyjęcie danych o użytkowniku
        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid

        // pobieranie kierunków z bazy
        val refForFields = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Fields")
        val postListenerForFields = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfFields.clear()
                for (i in dataSnapshot.children){
                    listOfFields.add(i.value as String)
                }

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerAddSubjectField.adapter = arrayAdapter
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refForFields.addValueEventListener(postListenerForFields)

        for ((counter, i) in listOfFields.withIndex()){
            if (i == sub.field){
                spinnerAddSubjectField.setSelection(counter)
            }
        }

        // dodawanie nowej formy przedmiotu
        buttonAddSubject_AddForm.setOnClickListener {
            listOfForms.add(Form())
            myAdapter.notifyDataSetChanged()
        }

        recyclerviewListOfForms.adapter = myAdapter
        recyclerviewListOfForms.layoutManager = LinearLayoutManager(this)

        buttonAddSubject.setOnClickListener {

            //dodawanie przedmiotu do bazy
            var isEveryThingOk: Boolean = true

            val userId: String = FirebaseAuth.getInstance().currentUser.uid

            val name = edittextAddSubjectName.text.toString()
            val ects = edittextECTS.text.toString().toInt()
            val field = spinnerAddSubjectField.selectedItem.toString()

            if (name == ""){
                isEveryThingOk = false
            }

            if (isEveryThingOk){
                for (i in listOfForms){
                    Log.d(i.form.toString(), "Info")
                    Log.d(i.howLong.toString(), "Info")
                    Log.d(i.hour.toString(), "Info")
                    Log.d(i.minute.toString(), "Info")
                    if (i.form == -1) {
                        isEveryThingOk = false
                        break
                    }
                    if (i.howLong == ""){
                        isEveryThingOk = false
                        break
                    }
                    if (i.hour == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.minute == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.dayOfWeek == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.dayOfWeek == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.dayStart == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.monthStart == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.yearStart == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.dayEnd == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.monthEnd == -1){
                        isEveryThingOk = false
                        break
                    }
                    if (i.yearEnd == -1){
                        isEveryThingOk = false
                        break
                    }
                }
            }

            if (isEveryThingOk){
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                val key = sub.id
                val newSubject = Subject(key, name, field, listOfForms, 0f, ects)
                if (key != null) {
                    ref.child(userId).child("Subjects").child(key).setValue(newSubject).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Zedytowano przedmiot", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Błąd", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Niektóre pola są wciąż puste", Toast.LENGTH_SHORT).show()
            }

            var intent = Intent(this,ListOfSubjects::class.java);
            startActivity(intent);

        }


    }

    override fun onDeleteClick(form: Form) {
        listOfForms.remove(form)
        myAdapter.notifyDataSetChanged()
    }

    override fun updateFields(): ArrayList<String> {
        return listOfFields
    }

    override fun showTimePicker() {
        val timePicker = TimePicker()
        timePicker.show(supportFragmentManager, "TimePicker")
    }

    override fun showDayStartPicker() {
        val dayPicker = DayPicker()
        dayPicker.show(supportFragmentManager, "DayStartPicker")
    }

    override fun showDayEndPicker() {
        val dayPicker = DayEndPicker()
        dayPicker.show(supportFragmentManager, "DayEndPicker")
    }

    override fun getPosition(position: Int) {
        positionOfCurrentForm = position
    }

    override fun sendFormInfo(info: Int) {
        listOfForms[positionOfCurrentForm].form = info
    }

    override fun sendDayOfWeekInfo(info: Int) {
        listOfForms[positionOfCurrentForm].dayOfWeek = info
    }

    override fun sendHowLong(info: String) {
        listOfForms[positionOfCurrentForm].howLong = info
    }

    override fun sendPickedTime(hour: Int, minute: Int) {
        TODO("Not yet implemented")
    }

    override fun onDismiss(dialog: DialogInterface?) {
        myAdapter.notifyDataSetChanged()
    }

    override fun getTime(hour: Int, minute: Int) {
        listOfForms[positionOfCurrentForm].hour = hour
        listOfForms[positionOfCurrentForm].minute = minute
    }

    override fun getDayStart(dayStart: Int, monthStart: Int, yearStart: Int) {
        listOfForms[positionOfCurrentForm].dayStart = dayStart
        listOfForms[positionOfCurrentForm].monthStart = monthStart
        listOfForms[positionOfCurrentForm].yearStart = yearStart
    }

    override fun getDayEnd(dayEnd: Int, monthEnd: Int, yearEnd: Int) {
        listOfForms[positionOfCurrentForm].dayEnd = dayEnd
        listOfForms[positionOfCurrentForm].monthEnd = monthEnd
        listOfForms[positionOfCurrentForm].yearEnd = yearEnd
    }


}