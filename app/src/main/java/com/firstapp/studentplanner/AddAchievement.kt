package com.firstapp.studentplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firstapp.studentplanner.Classes.Achievement
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_achievement.*

class AddAchievement: BottomSheetDialogFragment() {

    private var title: String = ""
    private var description: String = ""
    lateinit var toSend: GetAchievement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        toSend = activity as GetAchievement
        return inflater.inflate(R.layout.dialog_add_achievement,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddAchievement.setOnClickListener {
            title = editTextAchievementTitle.text.toString()
            description = editTextAchievementDescription.text.toString()

            val achiev = Achievement(title, description)

            toSend.getAchievement(achiev)
            dismiss()

        }
    }
}