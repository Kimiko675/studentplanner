package com.firstapp.studentplanner

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.day_picker.*
import kotlinx.android.synthetic.main.dialog_add_subject.*

class DayPicker: DialogFragment() {

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    private lateinit var picker: GetPickedTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        picker = activity as GetPickedTime
        return inflater.inflate(R.layout.day_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnOKDayPicker.setOnClickListener {
            day = dpDayPicker.dayOfMonth
            month = dpDayPicker.month
            year = dpDayPicker.year

            picker.getDayStart(day,month,year)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}