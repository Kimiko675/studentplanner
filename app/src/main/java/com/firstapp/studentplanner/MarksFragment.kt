package com.firstapp.studentplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MarksFragment(subject: Subject, userID: String): Fragment() {

    private var sub: Subject = subject
    private var userId: String = userID

    private lateinit var action: SetRecyclerViewMark


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action = activity as SetRecyclerViewMark
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detales_about_marks,container,false)
        view.findViewById<TextView>(R.id.textviewMark).text = sub.mark.toString()
        view.findViewById<TextView>(R.id.textviewECTS).text = sub.ects.toString()
        /*

        var listOfAchievements1 = ArrayList<Achievement>()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        val key = sub.id.toString()
        ref.child(userId).child("Subjects").child(key).child("achievement").get().addOnSuccessListener {
            if (it.value != null) {
                listOfAchievements1 = it.value as ArrayList<Achievement>
                //Log.d("TAG", listOfAchievements1.toString())

                view.findViewById<RecyclerView>(R.id.recyclerViewMarks).layoutManager = LinearLayoutManager(requireContext())
                view.findViewById<RecyclerView>(R.id.recyclerViewMarks).adapter = AchievementAdapter(listOfAchievements1, requireContext() as DetailActivity)
            }
        }

         */
        action.setAchievements()


        view.findViewById<TextView>(R.id.textviewMark).setOnClickListener {
            val markPicker = MarkPicker()
            markPicker.show(requireFragmentManager(), "MarkPicker")
        }

        val bottomSheetFragment = AddAchievement()

        view.findViewById<Button>(R.id.buttonAddNewAchievement).setOnClickListener {
            bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")
        }

        return view
    }

}

interface SetRecyclerViewMark{
    fun setAchievements()
}