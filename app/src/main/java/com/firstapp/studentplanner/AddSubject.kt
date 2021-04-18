package com.firstapp.studentplanner

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_of_fields.*
import kotlinx.android.synthetic.main.cyclical_subject.*
import kotlinx.android.synthetic.main.dialog_add_field.*
import kotlinx.android.synthetic.main.dialog_add_subject.*
import kotlinx.android.synthetic.main.onetime_subject.*
import kotlinx.android.synthetic.main.time_picker.*

class AddSubject: BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth;

    private var hourSetted: Int = -1
    private var minuteSetted: Int = -1

    private var dayStartSetted: Int = 0
    private var monthStartSetted: Int = 0
    private var yearStartSetted: Int = 0

    private var dayEndSetted: Int = 0
    private var monthEndSetted: Int = 0
    private var yearEndSetted: Int = 0

    private var dayOfWeek:Int = -1

    private var isCyclical: Boolean = true

    var timeFlag: Boolean = false
    var dayStartFlag: Boolean = false
    var dayEndFlag: Boolean = false
    var daySingleFlag: Boolean = false


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

        val layoutToInflate1 = this.layoutInflater.inflate(R.layout.cyclical_subject, null)
        daysOrCalendar.addView(layoutToInflate1)

        btnDayStartPicker.text = ""
        btnDayEndPicker.text = ""

        spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long){
                dayOfWeek = spinnerDay.selectedItemPosition
            }
        }

        btnDayStartPicker.setOnClickListener {

            val dayPicker1 = DayPicker()
            activity?.let { it1 -> dayPicker1.show(it1.supportFragmentManager, "Dialog")
            }

            timeFlag = false
            dayStartFlag = true
            dayEndFlag = false
            daySingleFlag = false
        }

        btnDayEndPicker.setOnClickListener {

            val dayPicker2 = DayPicker()
            activity?.let { it1 -> dayPicker2.show(it1.supportFragmentManager, "Dialog")
            }

            timeFlag = false
            dayStartFlag = false
            dayEndFlag = true
            daySingleFlag = false
        }

        val layoutToInflate2 = this.layoutInflater.inflate(R.layout.onetime_subject, null)


        radio_one.setOnClickListener {
            daysOrCalendar.removeAllViews()
            daysOrCalendar.addView(layoutToInflate1)

            isCyclical = true

            btnDayStartPicker.text = ""
            btnDayEndPicker.text = ""

            spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long){
                    dayOfWeek = spinnerDay.selectedItemPosition
                }
            }

            btnDayStartPicker.setOnClickListener {

                val dayPicker1 = DayPicker()
                activity?.let { it1 -> dayPicker1.show(it1.supportFragmentManager, "Dialog")
                }

                timeFlag = false
                dayStartFlag = true
                dayEndFlag = false
                daySingleFlag = false
            }

            btnDayEndPicker.setOnClickListener {

                val dayPicker2 = DayPicker()
                activity?.let { it1 -> dayPicker2.show(it1.supportFragmentManager, "Dialog")
                }

                timeFlag = false
                dayStartFlag = false
                dayEndFlag = true
                daySingleFlag = false
            }
        }

        radio_two.setOnClickListener {
            daysOrCalendar.removeAllViews()
            daysOrCalendar.addView(layoutToInflate2)

            isCyclical = false
            btnSingleDayPicker.text = ""

            btnSingleDayPicker.setOnClickListener {
                val dayPicker3 = DayPicker()
                activity?.let { it1 -> dayPicker3.show(it1.supportFragmentManager, "Dialog")
                }

                timeFlag = false
                dayStartFlag = false
                dayEndFlag = false
                daySingleFlag = true
            }
        }

        btnTimePicker.setOnClickListener() {

            val timePicker = TimePicker()

            activity?.let { it1 -> timePicker.show(it1.supportFragmentManager, "Dialog")
            }

            timeFlag = true
            dayStartFlag = false
            dayEndFlag = false
            daySingleFlag = false
        }

        buttonAddSubject.setOnClickListener{
            // tutaj trzeba zrobic dodawanie przedmiotu do bazy

            val userId: String = FirebaseAuth.getInstance().currentUser.uid

            val name = editTextFieldNameSubject.text.toString()
            val field = spinnerFields.selectedItem.toString()
            val form = spinnerForm.selectedItemPosition
            val howLong = editTextSubjectTime.text.toString()

            var isNotEmptyName: Boolean = false
            var isNotEmptyHowLong: Boolean = false
            var isNotEmptyHour: Boolean = false
            //var isNotEmptyMinute: Boolean = false
            var isNotEmptyDayStart: Boolean = false
            //var isNotEmptyMonthStart: Boolean = false
            //var isNotEmptyYearStart: Boolean = false
            var isNotEmptyDayEnd: Boolean = false
            //var isNotEmptyMonthEnd: Boolean = false
            //var isNotEmptyYearEnd: Boolean = false

            //sprawdzamy czy dane pola są uzupełnione
            if (name.isNotEmpty()) isNotEmptyName = true else Toast.makeText(context, "Podaj nazwę", Toast.LENGTH_SHORT).show()

            if (howLong.isNotEmpty()){
                isNotEmptyHowLong = true
            }else{
                Toast.makeText(context, "Podaj czas", Toast.LENGTH_SHORT).show()
            }
            if (hourSetted != -1){
                isNotEmptyHour = true
            }else{
                Toast.makeText(context, "Podaj godzinę rozpoczęcia", Toast.LENGTH_SHORT).show()
            }
            if (dayStartSetted != 0){
                isNotEmptyDayStart = true
            }else{
                Toast.makeText(context, "Podaj datę rozpoczęcia", Toast.LENGTH_SHORT).show()
            }
            if (dayEndSetted != 0){
                isNotEmptyDayEnd = true
            }else{
                Toast.makeText(context, "Podaj datę zakończenia", Toast.LENGTH_SHORT).show()
            }

            if (isNotEmptyName && isNotEmptyHowLong && isNotEmptyHour && isNotEmptyDayStart && isNotEmptyDayEnd){
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                val newRef = ref.push()
                val key = newRef.key
                val newSubject=Subject(key, name, field, form, howLong, isCyclical, hourSetted, minuteSetted, dayOfWeek, dayStartSetted, monthStartSetted, yearStartSetted, dayEndSetted, monthEndSetted, yearEndSetted, 0)
                if (key != null) {
                    ref.child(userId).child("Subjects").child(key).setValue(newSubject).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Dodano przedmiot", Toast.LENGTH_LONG).show()
                            dismiss()
                        } else {
                            Toast.makeText(activity, "Błąd", Toast.LENGTH_LONG).show()
                            dismiss()
                        }
                    }
                }
                //dismiss()
            }



            /* zmienne z pickerow
            hourSetted / minuteSetted - godzina i minuta rozpoczecia
            dayStartSetted / monthStartSetted / yearStartSetted - w cyklicznie > data pierwszego spotkania / w jednorazowo > data pojedynczego spotkania
            dayEndSetted / monthEndSetted / yearEndSetted - w cylkicznie > data ostatniego spotkania



             */



        }
    }

    public fun displayTime(){

        if (timeFlag && arguments != null) {
            hourSetted = arguments?.getInt("hour")!!
            minuteSetted = arguments?.getInt("minute")!!

            btnTimePicker.text = hourSetted.toString() + ":" + minuteSetted.toString()
        }
    }

    public fun displayDayStart(){

        if (dayStartFlag && arguments != null){
            dayStartSetted = arguments?.getInt("dayStart")!!
            monthStartSetted = arguments?.getInt("monthStart")!!
            monthStartSetted += 1
            yearStartSetted = arguments?.getInt("yearStart")!!

            btnDayStartPicker.text = dayStartSetted.toString() + "/" + monthStartSetted.toString() + "/" + yearStartSetted.toString()
        }
    }

    public fun displayDayEnd(){

        if (dayEndFlag && arguments != null){
            dayEndSetted = arguments?.getInt("dayStart")!!
            monthEndSetted = arguments?.getInt("monthStart")!!
            monthEndSetted += 1
            yearEndSetted = arguments?.getInt("yearStart")!!

            btnDayEndPicker.text = dayEndSetted.toString() + "/" + monthEndSetted.toString() + "/" + yearEndSetted.toString()
        }

    }

    public fun displayDaySingle(){
        if (daySingleFlag && arguments != null){
            dayStartSetted = arguments?.getInt("dayStart")!!
            monthStartSetted = arguments?.getInt("monthStart")!!
            monthStartSetted += 1
            yearStartSetted = arguments?.getInt("yearStart")!!

            btnSingleDayPicker.text = dayStartSetted.toString() + "/" + monthStartSetted.toString() + "/" + yearStartSetted.toString()
        }
    }


}
