package com.firstapp.studentplanner

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.time_picker.*


class TimePicker: DialogFragment() {

    private var hour: Int = 0
    private var minute: Int = 0

    // interfejs do przesyłania danych do AddNewSubject EditExistingSubject HomeworkActivity
    private lateinit var picker: GetPickedTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        picker = activity as GetPickedTime
        return inflater.inflate(R.layout.time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tpTimePicker.setIs24HourView(true)



        btnOKTimePicker.setOnClickListener{
            hour = tpTimePicker.hour
            minute = tpTimePicker.minute

            // przesyłanie danych
            picker.getTime(hour, minute)
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}