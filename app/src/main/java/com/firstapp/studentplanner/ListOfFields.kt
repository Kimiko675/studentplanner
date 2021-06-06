package com.firstapp.studentplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.studentplanner.Classes.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_list_of_fields.*
import kotlinx.android.synthetic.main.activity_list_of_subjects.*

class ListOfFields : AppCompatActivity(), OnFieldItemClickListener {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_fields)
        //połączenie z Firebase
        auth = FirebaseAuth.getInstance();
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Fields")
        val list = mutableListOf<String>()
        llFields.layoutManager = LinearLayoutManager(this)
        llFields.adapter = FieldsAdapter(list, this)

        //pobieranie danych z bazy i przekazywanie ich do adaptera
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                for (i in dataSnapshot.children){
                    list.add(i.value as String)
                }
                llFields.adapter = FieldsAdapter(list, this@ListOfFields)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

    //uruchamianie kolejnej aktywności i przekazywanie do niej danych
    override fun onItemClick(field: String) {
        val intent= Intent(this, ListOfMarks::class.java)
        intent.putExtra("field", field)
        startActivity(intent)
    }

    //usuwanie elementu
    override fun onDeleteClick(field: String) {
        val userId: String = FirebaseAuth.getInstance().currentUser.uid
        val ref2 = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Subjects")
        var flag: Boolean = false
        val list = mutableListOf<Subject>()

        val postListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (i in dataSnapshot.children){
                    val model= i.getValue(Subject::class.java)
                    i.key?.let { Log.d("Tab", it) }
                    list.add(model as Subject)
                }
                //zablokowanie przed usunięciem kierunku, który ma przypisane przedmioty
                for (i in list){
                    Log.d("PETLA", i.field.toString())
                    if (i.field == field){
                        Toast.makeText(this@ListOfFields, "Nie można usunąć kierunku gdy posiadasz podpięte do niego przedmioty", Toast.LENGTH_LONG).show()
                        flag = true
                        break
                    }
                }

                //jeśli usuwanie nie zostało zablokowane to wyszkujemy odpowiedni element w bazie i go usuwamy
                if (!flag){
                    val ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Fields")
                    var key: String = ""
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // Get Post object and use the values to update the UI
                            for (i in dataSnapshot.children){
                                if (field == i.value as String){
                                    key = i.key as String
                                    break
                                }
                            }
                            ref.child(key).removeValue()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                        }
                    }
                    ref.addValueEventListener(postListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref2.addValueEventListener(postListener2)
    }
}
