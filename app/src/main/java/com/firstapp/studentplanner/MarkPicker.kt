package com.firstapp.studentplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.detales_about_marks.*
import kotlinx.android.synthetic.main.mark_picker.*

class MarkPicker: DialogFragment() {

    private var mark: Float = 0f

    private lateinit var picker: GetPickedMark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        picker = activity as GetPickedMark
        return inflater.inflate(R.layout.mark_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkboxMark.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                sliderMark.visibility = View.VISIBLE
            }else{
                sliderMark.visibility = View.INVISIBLE
            }
        }

        btnOKMarkPicker.setOnClickListener {
            if (checkboxMark.isChecked) {
                mark = sliderMark.value
            }else{
                mark = 0f
            }
            picker.getMark(mark)
            dismiss()
        }
    }

}