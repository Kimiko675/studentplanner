package com.firstapp.studentplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.studentplanner.Classes.Achievement
import com.firstapp.studentplanner.Classes.Form
import com.firstapp.studentplanner.Classes.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_of_subjects.*
import kotlinx.android.synthetic.main.detales_about_marks.*
import kotlinx.android.synthetic.main.detales_about_subject.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item2.view.*

class DetailActivity : AppCompatActivity(), GetPickedMark, GetAchievement, OnAchievementItemClickListener, SetRecyclerViewMark {

    private var listOfForms = ArrayList<Form>()
    private lateinit var auth: FirebaseAuth;
    lateinit var userId: String
    private lateinit var sub: Subject

    private var listOfAchievement = ArrayList<Achievement>()
    var adapter = AchievementAdapter(listOfAchievement, this)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.detales_about_subject)

            auth = FirebaseAuth.getInstance();
            userId = FirebaseAuth.getInstance().currentUser.uid

            sub = intent.getSerializableExtra("subject") as Subject;

            tvTitleNameOfSubject.text = sub.subject
            textviewField.text = sub.field

            for (i in sub.forms!!){
                listOfForms.add(i)
            }

            //val Frag1 = FormsFragment(listOfForms)



            val viewPager = ViewPagerAdapter(supportFragmentManager)
            viewPager.addFragment(FormsFragment(listOfForms), "Formy")
            viewPager.addFragment(MarksFragment(sub,userId), "Oceny")
            viewPagerFormsMarks.adapter = viewPager
            navigation_menu.setupWithViewPager(viewPagerFormsMarks)





            //myAdapter.notifyDataSetChanged()


            //recyclerviewListOfAllForms.adapter = myAdapter
            //recyclerviewListOfAllForms.layoutManager = LinearLayoutManager(this)






            /* val s1:String = intent.getStringExtra("subject").toString()
             val s2:String = intent.getStringExtra("field").toString()
             val i11:Int = intent.getIntExtra("form",0)
             val i10:Int = intent.getIntExtra("dayWeek", 0)
             val i1: Int = intent.getIntExtra("hour",0)
             val i2: Int = intent.getIntExtra("minute",0)
             val i3: Int = intent.getIntExtra("dayStart",0)
             val i5: Int = intent.getIntExtra("monthStart",0)
             val i6: Int = intent.getIntExtra("yearStart",0)
             val i7: Int = intent.getIntExtra("dayEnd",0)
             val i8: Int = intent.getIntExtra("monthEnd",0)
             val i9: Int = intent.getIntExtra("yearEnd",0)
             val s3: String = intent.getStringExtra("time").toString()

             var forms = arrayListOf<String>("wykład","ćwiczenia","seminarium","spotkanie","zebranie","rejestracja","inne")
             var dni = arrayListOf<String>("poniedziałek","wtorek","środa","czwartek","piątek","sobota","niedziela")
             textForm.text = forms[i11]
             textDay.text = dni[i10]
             textSubject.text = s1
             textField.text = s2
             textHour.text = i1.toString()
             textMinute.text = i2.toString()
             textDayStart.text = i3.toString()
             textMonthStart.text = i5.toString()
             textYearStart.text = i6.toString()
             textDayEnd.text = i7.toString()
             textMonthEnd.text = i8.toString()
             textYearEnd.text = i9.toString()
             textTime.text = s3*/
        }

    class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItem(position: Int): Fragment{
            return mFragmentList[position]
        }

        fun addFragment(fragment: Fragment, title: String){
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun getMark(mark: Float) {
        if (mark == 0f){
            textviewMark.text = "-"
        }else{
            textviewMark.text = mark.toString()
        }
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        val key = sub.id.toString()
        ref.child(userId).child("Subjects").child(key).child("mark").setValue(mark)
    }

    override fun getAchievement(achievement: Achievement) {
        // dokończyć

        var needToAdd: Boolean = true

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects").child(sub.id.toString()).child("achievement")
        val postListenerForSubjects = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfAchievement.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Achievement::class.java)
                    listOfAchievement.add(model as Achievement)
                }
                if (needToAdd) {
                    listOfAchievement.add(achievement)

                    recyclerViewMarks.layoutManager = LinearLayoutManager(this@DetailActivity)
                    recyclerViewMarks.adapter = AchievementAdapter(listOfAchievement, this@DetailActivity)
                    ref.setValue(listOfAchievement)
                    needToAdd = false
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListenerForSubjects)


    }

    override fun onDeleteClick(achievement: Achievement) {
        listOfAchievement.remove(achievement)
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects").child(sub.id.toString()).child("achievement")
        ref.setValue(listOfAchievement)
        adapter.notifyDataSetChanged()
    }

    override fun setAchievements() {

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects").child(sub.id.toString()).child("achievement")
        val postListenerForSubjects = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfAchievement.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Achievement::class.java)
                    listOfAchievement.add(model as Achievement)
                }

                recyclerViewMarks.layoutManager = LinearLayoutManager(this@DetailActivity)
                recyclerViewMarks.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListenerForSubjects)
    }
}


